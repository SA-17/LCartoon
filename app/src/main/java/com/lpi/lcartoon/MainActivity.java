package com.lpi.lcartoon;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.lpi.lcartoon.Activties.Info;
import com.lpi.lcartoon.CL.OnEditTextRightDrawableTouchListener;
import com.lpi.lcartoon.Data.Data;
import com.lpi.lcartoon.Data.Data1;
import com.lpi.lcartoon.Data.Data2;
import com.lpi.lcartoon.Data.Data3;

import com.lpi.lcartoon.Data.Data4;
import javax.security.auth.callback.Callback;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import android.content.res.Configuration;

public class MainActivity extends AppCompatActivity {

	ListView lv;
	EditText sv;
	MyListAdapter adapter;
	ActionMode mActionMode;

	HashMap<String, String> cartoonn;
	HashMap<String, String> cartoont;
	HashMap<String, String> cartoons;
	HashMap<String, String> cartoone;
	HashMap<String, String> cartoonl;

	ArrayList<Data> cartoonname = new ArrayList<Data>();
	ArrayList<Data1> cartoontype = new ArrayList<Data1>();
	ArrayList<Data2> cartoonseason = new ArrayList<Data2>();
	ArrayList<Data3> cartoonepisode = new ArrayList<Data3>();
	ArrayList<Data4> cartoonlink = new ArrayList<Data4>();

	String[] permissions = { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE };

	String rootpath = Environment.getExternalStorageDirectory().toString();
	String gljson = rootpath + "/cartoon.xml";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int nightModeFlags = getApplicationContext().getResources().getConfiguration().uiMode
				& Configuration.UI_MODE_NIGHT_MASK;
		switch (nightModeFlags) {
		case Configuration.UI_MODE_NIGHT_YES:
			setTheme(R.style.Theme_MyApplicationDark);
			break;

		case Configuration.UI_MODE_NIGHT_NO:
			setTheme(R.style.Theme_MyApplication);
			break;

		case Configuration.UI_MODE_NIGHT_UNDEFINED:
			setTheme(R.style.Theme_MyApplicationDark);
			break;
		}

		setContentView(R.layout.activity_main);
		// init comps
		lv = findViewById(R.id.lcartoon);
		sv = findViewById(R.id.scartoon);

		if ((ContextCompat.checkSelfPermission(MainActivity.this,
				Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
				|| (ContextCompat.checkSelfPermission(MainActivity.this,
						Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
			// create dir
			File f = new File(rootpath + "/LCartoon");
			if (!f.exists()) {
				f.mkdir();
			} else {

			}

			File f1 = new File(gljson);

			if (!f1.exists()) {
				// download stuff
				Toast.makeText(MainActivity.this, "config.xml not found", Toast.LENGTH_SHORT).show();
			}

			ArrayList<String> name0 = new ArrayList<String>();
			ArrayList<String> type0 = new ArrayList<String>();
			ArrayList<String> season0 = new ArrayList<String>();
			ArrayList<String> episode0 = new ArrayList<String>();
			ArrayList<String> link0 = new ArrayList<String>();

			try {
				InputStream istream = new FileInputStream(f1.getAbsolutePath());
				DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
				Document doc = docBuilder.parse(istream);
				NodeList nList = doc.getElementsByTagName("cartoon");
				for (int i = 0; i < nList.getLength(); i++) {
					if (nList.item(0).getNodeType() == Node.ELEMENT_NODE) {

						cartoonn = new HashMap<>();
						cartoont = new HashMap<>();
						cartoons = new HashMap<>();
						cartoone = new HashMap<>();
						cartoonl = new HashMap<>();

						Element elm = (Element) nList.item(i);

						cartoonn.put("cname", getNodeValue("name", elm));
						cartoont.put("ctype", getNodeValue("type", elm));
						cartoons.put("cseason", getNodeValue("season", elm));
						cartoone.put("cepisode", getNodeValue("episode", elm));
						cartoonl.put("clink", getNodeValue("link", elm));

						name0.add(cartoonn.values().toString());
						type0.add(cartoont.values().toString());
						season0.add(cartoons.values().toString());
						episode0.add(cartoone.values().toString());
						link0.add(cartoonl.values().toString());
					}
				}

				for (int i = 0; i < name0.size(); i++) {
					if (name0.size() == 0) {

					} else {
						String nc = name0.get(i).substring(1, name0.get(i).length() - 1);
						String tc = type0.get(i).substring(1, type0.get(i).length() - 1);
						String sc = season0.get(i).substring(1, season0.get(i).length() - 1);
						String ec = episode0.get(i).substring(1, episode0.get(i).length() - 1);
						String lc = link0.get(i).substring(1, episode0.get(i).length() - 1);

						name0.set(i, nc);
						type0.set(i, tc);
						season0.set(i, sc);
						episode0.set(i, ec);
						link0.set(i, ec);
					}
				}

				cartoonname = new ArrayList<Data>();
				cartoontype = new ArrayList<Data1>();
				cartoonseason = new ArrayList<Data2>();
				cartoonepisode = new ArrayList<Data3>();
				cartoonlink = new ArrayList<Data4>();

				for (int i = 0; i < name0.size(); i++) {
					Data nm = new Data(name0.get(i));
					// Binds all strings into an array
					cartoonname.add(nm);

					Data1 nm0 = new Data1(type0.get(i));
					// Binds all strings into an array
					cartoontype.add(nm0);

					Data2 nm1 = new Data2(season0.get(i));
					// Binds all strings into an array
					cartoonseason.add(nm1);

					Data3 nm2 = new Data3(episode0.get(i));
					// Binds all strings into an array
					cartoonepisode.add(nm2);

					Data4 nm3 = new Data4(link0.get(i));
					// Binds all strings into an array
					cartoonlink.add(nm3);
				}

				//   Toast.makeText(MainActivity.this, volume0.toString(),
				// Toast.LENGTH_SHORT).show();								

				adapter = new MyListAdapter(MainActivity.this);

				lv.setAdapter(adapter);

				lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> ad, View v, final int ol, long l) {
						Intent intent = new Intent(MainActivity.this, Info.class);
						intent.putExtra("NAME", cartoonname.get(ol).getName());
						intent.putExtra("TYPE", cartoontype.get(ol).getType());
						intent.putExtra("SEASON", cartoonseason.get(ol).getSeason());
						intent.putExtra("EPISODE", cartoonepisode.get(ol).getEpisode());
						intent.putExtra("LINK", cartoonlink.get(ol).getLink());
						startActivity(intent);
					}
				});

				sv.setOnLongClickListener(new View.OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {
						mActionMode = MainActivity.this.startActionMode(new ActionBarCallback());
						return true;
					}
				});

				sv.addTextChangedListener(new TextWatcher() {
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {

					}

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						if (s.toString().equals("")) {
							sv.setCompoundDrawablesWithIntrinsicBounds(
									ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_search_black_24dp), null,
									null, null);
						} else {
							sv.setCompoundDrawablesWithIntrinsicBounds(
									ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_search_black_24dp), null,
									ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_close_black_24dp), null);
						}
						adapter.filter(s.toString());

						sv.setOnTouchListener(new OnEditTextRightDrawableTouchListener(sv) {
							@Override
							public void OnDrawableClick() {
								if (sv.getText().toString().equals("")) {
									sv.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(
											MainActivity.this, R.drawable.ic_search_black_24dp), null, null, null);
								} else {
									sv.getText().clear();
									InputMethodManager inputManager = (InputMethodManager) getApplicationContext()
											.getSystemService(Context.INPUT_METHOD_SERVICE);
									inputManager.hideSoftInputFromWindow(sv.getWindowToken(), 0);
								}
							}
						});

					}

					@Override
					public void afterTextChanged(Editable s) {

					}
				});

			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			}

		} else {
			// request permission
			ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);

			if ((ContextCompat.checkSelfPermission(MainActivity.this,
					Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
					|| (ContextCompat.checkSelfPermission(MainActivity.this,
							Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
				// good = is granted
			} else {
				AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
				b.setTitle("Error");
				b.setMessage("Please Grant Permission");
				b.setCancelable(false);

				b.setPositiveButton("yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface di, int in) {
						Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
						Uri uri = Uri.fromParts("package", getPackageName(), null);
						intent.setData(uri);
						startActivity(intent);
						finish();
					}
				});

				b.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface di, int in) {
						finish();
					}
				});

				b.show();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		if ((ContextCompat.checkSelfPermission(MainActivity.this,
				Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
				|| (ContextCompat.checkSelfPermission(MainActivity.this,
						Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
			// create dir
			File f = new File(rootpath + "/LCartoon");
			File cfg = new File(gljson);
			if (!f.exists()) {
				f.mkdir();
			}

		} else {

		}

	}

	protected String getNodeValue(String tag, Element element) {
		NodeList nodeList = element.getElementsByTagName(tag);
		Node node = nodeList.item(0);
		if (node != null) {
			if (node.hasChildNodes()) {
				Node child = node.getFirstChild();
				while (child != null) {
					if (child.getNodeType() == Node.TEXT_NODE) {
						return child.getNodeValue();
					}
				}
			}
		}
		return "";
	}

	public class MyListAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<Data> name;
		private ArrayList<Data1> type;
		private ArrayList<Data2> season;
		private ArrayList<Data3> episode;
		private ArrayList<Data4> link;

		LayoutInflater inflater;

		public MyListAdapter(Context context) {
			context = context;
			inflater = LayoutInflater.from(context);

			this.name = new ArrayList<Data>();
			this.name.addAll(cartoonname);

			this.type = new ArrayList<Data1>();
			this.type.addAll(cartoontype);

			this.season = new ArrayList<Data2>();
			this.season.addAll(cartoonseason);

			this.episode = new ArrayList<Data3>();
			this.episode.addAll(cartoonepisode);

			this.link = new ArrayList<Data4>();
			this.link.addAll(cartoonlink);
		}

		public class ViewHolder {
			TextView tvname;
			TextView tvtype;
			TextView tve, tvs;
			TextView tvse, tvep;
			TextView sl;
		}

		@Override
		public int getCount() {
			return cartoonname.size();
		}

		@Override
		public Data getItem(int position) {
			return cartoonname.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View view, ViewGroup parent) {
			final ViewHolder holder;
			if (view == null) {
				holder = new ViewHolder();
				view = inflater.inflate(R.layout.list_row, null);
				// Locate the TextViews in list_row.xml
				holder.tvname = (TextView) view.findViewById(R.id.cname);
				holder.tvtype = (TextView) view.findViewById(R.id.ctype);
				holder.tve = (TextView) view.findViewById(R.id.e);
				holder.tvs = (TextView) view.findViewById(R.id.s);
				holder.tvse = (TextView) view.findViewById(R.id.cseason);
				holder.tvep = (TextView) view.findViewById(R.id.cepisode);
				holder.sl = (TextView) view.findViewById(R.id.sl);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			// Set the results into TextViews
			holder.tvname.setText(cartoonname.get(position).getName());
			holder.tvtype.setText(cartoontype.get(position).getType());

			// checks for type

			if (cartoontype.get(position).getType().equals("film")) {

				holder.tve.setVisibility(View.GONE);
				holder.tvs.setVisibility(View.GONE);
				holder.tvse.setVisibility(View.GONE);
				holder.tvep.setVisibility(View.GONE);
				holder.sl.setVisibility(View.GONE);

			} else if (cartoontype.get(position).getType().equals("comic")) {

				if (cartoonseason.get(position).getSeason().equals("")) {
					holder.tvs.setVisibility(View.GONE);
					holder.tvse.setVisibility(View.GONE);
				} else {
					holder.tvs.setVisibility(View.VISIBLE);
					holder.tvse.setVisibility(View.VISIBLE);
					holder.tvs.setText("Season: ");
					holder.tvse.setText(cartoonseason.get(position).getSeason());
				}

				if (cartoonepisode.get(position).getEpisode().equals("")) {
					holder.tve.setVisibility(View.GONE);
					holder.tvep.setVisibility(View.GONE);
				} else {
					holder.tve.setVisibility(View.VISIBLE);
					holder.tvep.setVisibility(View.VISIBLE);
					holder.tve.setText("Volume: ");
					holder.tvep.setText(cartoonepisode.get(position).getEpisode());
				}

				if (cartoonseason.get(position).getSeason().equals("")
						&& cartoonepisode.get(position).getEpisode().equals("")) {
					holder.sl.setVisibility(View.GONE);
				} else {
					holder.sl.setVisibility(View.VISIBLE);
				}

			} else if (cartoontype.get(position).getType().equals("cartoon")) {

				if (cartoonseason.get(position).getSeason().equals("")) {
					holder.tvs.setVisibility(View.GONE);
					holder.tvse.setVisibility(View.GONE);
				} else {
					holder.tvs.setVisibility(View.VISIBLE);
					holder.tvse.setVisibility(View.VISIBLE);
					holder.tvs.setText("Season: ");
					holder.tvse.setText(cartoonseason.get(position).getSeason());
				}

				if (cartoonepisode.get(position).getEpisode().equals("")) {
					holder.tve.setVisibility(View.GONE);
					holder.tvep.setVisibility(View.GONE);
				} else {
					holder.tve.setVisibility(View.VISIBLE);
					holder.tvep.setVisibility(View.VISIBLE);
					holder.tve.setText("Episode: ");
					holder.tvep.setText(cartoonepisode.get(position).getEpisode());
				}

				if (cartoonseason.get(position).getSeason().equals("")
						&& cartoonepisode.get(position).getEpisode().equals("")) {
					holder.sl.setVisibility(View.GONE);
				} else {
					holder.sl.setVisibility(View.VISIBLE);
				}

			} else {

				holder.tve.setVisibility(View.GONE);
				holder.tvs.setVisibility(View.GONE);
				holder.tvep.setVisibility(View.GONE);
				holder.tvse.setVisibility(View.GONE);
				holder.sl.setVisibility(View.GONE);
			}

			return view;
		}

		public void filter(String charText) {
			try {
				charText = charText.toLowerCase(Locale.getDefault());
				cartoonname.clear();
				cartoontype.clear();
				cartoonseason.clear();
				cartoonepisode.clear();
				cartoonlink.clear();
				if (charText.length() == 0) {
					cartoonname.addAll(name);
					cartoontype.addAll(type);
					cartoonseason.addAll(season);
					cartoonepisode.addAll(episode);
					cartoonlink.addAll(link);
				} else {
					for (int i = 0; i < name.size(); i++) {
						Data wp = name.get(i);
						if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
							cartoonname.add(wp);
							Data1 tpn = new Data1(type.get(i).getType());
							cartoontype.add(tpn);
							Data2 tps = new Data2(season.get(i).getSeason());
							cartoonseason.add(tps);
							Data3 tpe = new Data3(episode.get(i).getEpisode());
							cartoonepisode.add(tpe);
							Data4 tpl = new Data4(link.get(i).getLink());
							cartoonlink.add(tpl);
						}
					}
				}
				notifyDataSetChanged();
			} catch (Exception e) {
				Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
			}
		}
	}

	class ActionBarCallback implements ActionMode.Callback {

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			mode.getMenuInflater().inflate(R.menu.editpopup, menu);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

			int id = item.getItemId();
			switch (id) {
			case R.id.item_copy:
			    Toast.makeText(MainActivity.this, "copy", Toast.LENGTH_SHORT).show();
				break;
			}
			return false;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {

		}
	}

}

/*

 ****To Do:
 *
 * 1) check if season/episode is typed.. (good) tested: 70%
 * 2) add links stuff
 * 3) add download manager
 * 4) add video watcher
 * 5) add filter mode (cartoon/comic/unknown/film)
 * 6) add config in fab with fm manager
 * 7) add maintainer part + stuff to check if is dubbed + lang
 * 8) complete search method (good) 90% need app' on hide keyboard after clicking close btn
 * 9) add copy/past
*/
