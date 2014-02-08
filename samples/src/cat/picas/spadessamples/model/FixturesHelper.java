/**
 * Copyright (C) 2013 Pau Picas Sans <pau.picas@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

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
		personB.spouse.set(personA);
		personDao.insert(personB);

		// Person C.
		Person personC = new Person();
		personC.setName("Lorem Vadecun");
		personDao.insert(personC);

		// Set person B as person A spouse.
		personA.spouse.set(personB);
		personDao.save(personA);

		// Person A main contact point.
		ContactPoint contactPoint = new ContactPoint();
		contactPoint.setName("Main");
		contactPoint.setEmail("nulla@example.com");
		contactPoint.setPhone("111-222-333");
		contactPoint.person.set(personA);
		contactPointDao.insert(contactPoint);

		// Person A alternative email contact point.
		contactPoint = new ContactPoint();
		contactPoint.setName("Alternative email");
		contactPoint.setEmail("nulla_male@other.com");
		contactPoint.person.set(personA);
		contactPointDao.insert(contactPoint);
	}
}
