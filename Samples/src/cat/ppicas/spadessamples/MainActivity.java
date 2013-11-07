package cat.ppicas.spadessamples;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import cat.ppicas.spadessamples.model.DatabaseHelper;

public class MainActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ItemAdapter adapter = new ItemAdapter(this);
		setListAdapter(adapter);

		adapter.add(new Item("List with CursorAdapter", CursorListActivity.class));
		adapter.add(new Item("List with ArrayAdapter", null));

		getListView().setOnItemClickListener(new ItemClickListener());
	}

	private static class ItemAdapter extends ArrayAdapter<Item> {
		public ItemAdapter(Context context) {
			super(context, android.R.layout.simple_list_item_1);
		}
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
			ItemAdapter adapter = (ItemAdapter) parent.getAdapter();
			Item item = adapter.getItem(position);
			if (item.activity != null) {
				DatabaseHelper.deleteDatabase(context);
				context.startActivity(new Intent(context, item.activity));
			}
		}
	}

}
