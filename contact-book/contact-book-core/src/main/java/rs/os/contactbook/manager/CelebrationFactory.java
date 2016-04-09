package rs.os.contactbook.manager;

import rs.os.contactbook.model.common.Celebration;
import rs.os.core.factory.ObjectFactory;

public class CelebrationFactory extends ObjectFactory<Celebration> {

	@Override
	public Celebration createObject() {
		Celebration result = new Celebration();
		result.setLabel("mikin rodjendan");
		result.setType(Celebration.Type.Anniversary);
		result.setValue("svake godine isti dan");
		return result;
	}

}
