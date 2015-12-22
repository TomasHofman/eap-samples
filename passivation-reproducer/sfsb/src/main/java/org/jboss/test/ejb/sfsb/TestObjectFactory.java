package org.jboss.test.ejb.sfsb;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.WeakHashMap;

public class TestObjectFactory {

	public static Object newFairlyComplexObjectStructure() {
		ModelWithLink modelA = new ModelWithLink();
		for (int i = 0; i < 233; i++) modelA.addLinkedModel(new SimpleModel());
		for (int i = 0; i < 250; i++) modelA.criticalMethod();
		return modelA;
	}

	public static Object newObjectWithSerializationIssue() {
		return new Serializable() {
			private static final long serialVersionUID = 1L;
			private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
				throw new RuntimeException(new java.lang.StackOverflowError("Test"));
			}
		};

	}

	public static Object newObjectWithGuaranteedStackOverflow() {
		return new Serializable() {
			private static final long serialVersionUID = 1L;
			private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {
				writeObject(s);
			}
		};

	}

	/* ------------------------------------------------------------------------------------------- */
	static class ModelWithLink extends SimpleModel {
		private static final long serialVersionUID = 1L;
		private Link linkedModels = new Link();

		void addLinkedModel(SimpleModel pModel) {
			linkedModels.add(pModel);
		}

		void criticalMethod() {
			Link tmpLink = new Link();
			for (SimpleModel m : linkedModels.elements()) {
				tmpLink.add(m);
			}
		}
	}

	static class SimpleModel implements Serializable {

		private static final long serialVersionUID = 1L;
		private transient Map<Link, Object> mContainedInLink = new WeakHashMap<Link, Object>();

		void registerLink(Link pLink) {
			mContainedInLink.put(pLink, null);
		}

		private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException {

			s.defaultWriteObject();
			s.writeObject(new HashSet<Link>(mContainedInLink.keySet()));
		}

	}

	static class Link implements Serializable {

		private static final long serialVersionUID = 1L;
		private HashSet<SimpleModel> elements = new HashSet<SimpleModel>();

		public void add(SimpleModel pModel) {
			elements.add(pModel);
			pModel.registerLink(this);
		}

		public Collection<SimpleModel> elements() {
			return elements;
		}

	}
}
