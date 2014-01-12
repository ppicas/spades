package cat.picas.spadessamples.model;

import cat.picas.spadessamples.model.Person.Gender;
import android.database.sqlite.SQLiteDatabase;

public class FixturesHelper {
	public static void createFixtures(SQLiteDatabase db) {
		PersonDao personDao = new PersonDao(db);
		ContactPointDao contactPointDao = new ContactPointDao(db);

		// Person A.
		Person personA = new Person();
		personA.setName("Nulla Malesuada");
		personA.setGender(Gender.MALE);
		personA.setHeight(1.75);
		personA.setWeight(72);
		personDao.insert(personA);

		// Person B.
		Person personB = new Person();
		personB.setName("Ipsum Quia");
		personB.setGender(Gender.FEMALE);
		personB.setHeight(1.64);
		personB.setWeight(62);
		personB.getSpouse().set(personA);
		personDao.insert(personB);

		// Person C.
		Person personC = new Person();
		personC.setName("Lorem Vadecun");
		personDao.insert(personC);

		// Set person B as person A spouse.
		personA.getSpouse().set(personB);
		personDao.save(personA);

		// Person A main contact point.
		ContactPoint contactPoint = new ContactPoint();
		contactPoint.setName("Main");
		contactPoint.setEmail("nulla@example.com");
		contactPoint.setPhone("111-222-333");
		contactPoint.getPerson().set(personA);
		contactPointDao.insert(contactPoint);

		// Person A alternative email contact point.
		contactPoint = new ContactPoint();
		contactPoint.setName("Alternative email");
		contactPoint.setEmail("nulla_male@other.com");
		contactPoint.getPerson().set(personA);
		contactPointDao.insert(contactPoint);
	}
}
