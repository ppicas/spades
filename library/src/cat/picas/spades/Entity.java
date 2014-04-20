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

package cat.picas.spades;

/**
 * The classes implementing this interface will represent a database entity. In other words,
 * every instance of the class will represent a row of a database table.
 */
public interface Entity {

	/**
	 * Returns a {@code Long} representing the unique ID of this entity.
	 *
	 * @return Returns the unique ID of this Entity as {@code Long}, or {@code null} if this Entity doesn't
	 * have ID.
	 */
	public Long getEntityId();

	/**
	 * Change the unique ID of this Entity.
	 *
	 * @param id a {@code Long} on {@code null} if doesn't have ID
	 * @see #getEntityId()
	 */
	public void setEntityId(Long id);

}
