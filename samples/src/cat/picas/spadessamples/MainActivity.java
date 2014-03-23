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

package cat.picas.spadessamples;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

import cat.picas.spadessamples.model.DatabaseHelper;

public class MainActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ArrayAdapter<Item> adapter = new ArrayAdapter<Item>(this, android.R.layout.simple_list_item_1);
		setListAdapter(adapter);

		adapter.add(new Item("List with CursorAdapter", CursorListActivity.class));
		adapter.add(new Item("List with ArrayAdapter", ArrayListActivity.class));

		getListView().setOnItemClickListener(new ItemClickListener());
	}

	private static class Item {
		String name;
		Class<?> activity;

		public Item(String name, Class<?> activity) {
			this.name = name;
			this.activity = activity;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	private static class ItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Context context = parent.getContext();
			Item item = (Item) parent.getAdapter().getItem(position);
			if (item.activity != null) {
				DatabaseHelper.deleteDatabase(context);
				context.startActivity(new Intent(context, item.activity));
			}
		}
	}

}
