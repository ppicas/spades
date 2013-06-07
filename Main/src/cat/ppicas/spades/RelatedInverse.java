package cat.ppicas.spades;

import java.lang.reflect.Field;

public class RelatedInverse {

	private Field mRelatedField;
	private Field mKeyValueField;

	public RelatedInverse(Field relatedField, Field keyValueField) {
		mRelatedField = relatedField;
		mKeyValueField = keyValueField;

		if (!relatedField.getType().isAssignableFrom(Related.class)) {
			throw new IllegalArgumentException("The type of relatedField must be Related");
		}

		if (keyValueField.getType() != Long.TYPE && keyValueField.getType() != Long.class) {
			throw new IllegalArgumentException("The type of keyValueField must be Long or long");
		}

		relatedField.setAccessible(true);
		keyValueField.setAccessible(true);
	}

	public Field getRelatedField() {
		return mRelatedField;
	}

	public Field getKeyValueField() {
		return mKeyValueField;
	}

}
