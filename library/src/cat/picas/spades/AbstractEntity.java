package cat.picas.spades;

public abstract class AbstractEntity implements Entity {

	private Long mId;

	@Override
	public Long getEntityId() {
		return mId;
	}

	@Override
	public void setEntityId(Long id) {
		mId = id;
	}

}
