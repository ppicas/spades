package cat.ppicas.spades.map;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import cat.ppicas.spades.ColumnBuilder.ColumnType;
import cat.ppicas.spades.Related;

public class MappedFieldFactory {

	private static MappedFieldFactory sInstance;

	public static MappedFieldFactory getInstance() {
		if (sInstance == null) {
			sInstance = new MappedFieldFactory();
			registerCoreValueMappers(sInstance);
		}

		return sInstance;
	}

	private static void registerCoreValueMappers(MappedFieldFactory factory) {
		IntegerMapper integerMapper = new IntegerMapper();
		factory.registerValueMapper(Integer.TYPE, integerMapper, ColumnType.INTEGER);
		factory.registerValueMapper(Integer.class, integerMapper, ColumnType.INTEGER);

		LongMapper longMapper = new LongMapper();
		factory.registerValueMapper(Long.TYPE, longMapper, ColumnType.INTEGER);
		factory.registerValueMapper(Long.class, longMapper, ColumnType.INTEGER);

		DoubleMapper doubleMapper = new DoubleMapper();
		factory.registerValueMapper(Double.TYPE, doubleMapper, ColumnType.REAL);
		factory.registerValueMapper(Double.class, doubleMapper, ColumnType.REAL);

		BooleanMapper booleanMapper = new BooleanMapper();
		factory.registerValueMapper(Boolean.TYPE, booleanMapper, ColumnType.INTEGER);
		factory.registerValueMapper(Boolean.class, booleanMapper, ColumnType.INTEGER);

		factory.registerValueMapper(String.class, new StringMapper(), ColumnType.TEXT);

		factory.registerValueMapper(Date.class, new DateMapper(), ColumnType.INTEGER);

		factory.registerValueMapper(Related.class, new RelatedMapper(), ColumnType.INTEGER);

		factory.registerValueMapper(Enum.class, new EnumMapper(), ColumnType.TEXT);
	}

	private Map<Class<?>, ValueMapper> mValueMappers = new HashMap<Class<?>, ValueMapper>();
	private Map<Class<?>, ColumnType> mColumnTypes = new HashMap<Class<?>, ColumnType>();

	private MappedFieldFactory() {
	}

	public void registerValueMapper(Class<?> cls, ValueMapper valueMapper, ColumnType columnType) {
		mValueMappers.put(cls, valueMapper);
		mColumnTypes.put(cls, columnType);
	}

	public MappedField createForField(Field field) {
		ValueMapper valueMapper = mValueMappers.get(field.getType());
		ColumnType columnType = mColumnTypes.get(field.getType());

		if (valueMapper == null) {
			for (Class<?> cls : mValueMappers.keySet()) {
				if (cls.isAssignableFrom(field.getType())) {
					valueMapper = mValueMappers.get(cls);
					columnType = mColumnTypes.get(cls);
					break;
				}
			}
		}

		if (valueMapper == null) {
			throw new IllegalArgumentException("The type of the field '" + field.getName() + "' "
					+ " is not supported: " + field.getType().getName());
		}

		return new MappedFieldImpl(field, valueMapper, columnType);
	}

	private static class MappedFieldImpl implements MappedField {
		private Field mField;
		private ValueMapper mMapper;
		private ColumnType mColumnType;

		private MappedFieldImpl(Field field, ValueMapper mapper, ColumnType columnType) {
			mField = field;
			mMapper = mapper;
			mColumnType = columnType;

			mField.setAccessible(true);
		}

		@Override
		public void putContetValue(Object object, ContentValues values, String key, boolean notNull) {
			mMapper.putContetValue(mField, object, values, key, notNull);
		}

		@Override
		public void setFieldValue(Object object, Cursor cursor, int index) {
			mMapper.setFieldValue(mField, object, cursor, index);
		}

		@Override
		public Field getField() {
			return mField;
		}

		@Override
		public ColumnType getColumnType() {
			return mColumnType;
		}
	}

}
