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

package cat.picas.spades.fetch;

import java.util.List;

import android.database.Cursor;
import cat.picas.spades.CursorInfo;
import cat.picas.spades.Dao.EntityConsumer;
import cat.picas.spades.Entity;

public interface FetchStrategy<T extends Entity> {
	public List<T> fetchAll(Cursor cursor, CursorInfo cursorInfo, EntityConsumer<T> consumer);
}
