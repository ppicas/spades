package cat.ppicas.spadessamples;

import cat.ppicas.spadessamples.model.DatabaseHelper;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.cursor_list).setOnClickListener(new ClickListener(CursorListActivity.class));
		// findViewById(R.id.object_list).setOnClickListener(new ClickListener(MainActivity.class));
	}

	private class ClickListener implements OnClickListener {
		private Class<?> mActivity;

		public ClickListener(Class<?> activity) {
			mActivity = activity;
		}

		@Override
		public void onClick(View v) {
			DatabaseHelper.deleteDatabase(MainActivity.this);
			startActivity(new Intent(MainActivity.this, mActivity));
		}
	}

}
