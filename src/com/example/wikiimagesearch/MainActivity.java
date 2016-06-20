package com.example.wikiimagesearch;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements
		OnQueryTextListener {

	SearchView search;
	GridView grid;
	TextView noresult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		search = (SearchView) findViewById(R.id.searchView1);
		search.setQueryHint("Start typing to search...");
		search.setOnQueryTextListener(this);

		grid = (GridView) findViewById(R.id.imagesGridview);
		noresult = (TextView) findViewById(R.id.noResult);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		return false;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		if (newText.length() > 1) {

			// searchResults.setVisibility(myFragmentView.VISIBLE);
			grid.setVisibility(View.VISIBLE);
			myAsyncTask m = (myAsyncTask) new myAsyncTask().execute(newText);
		} else {

			grid.setVisibility(View.GONE);
			// searchResults.setVisibility(myFragmentView.INVISIBLE);
		}

		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	class myAsyncTask extends AsyncTask<String, Void, String> {
		// JSONParser jParser;
		JSONArray productList;
		String url = new String();
		String textSearch;
		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			productList = new JSONArray();
			// jParser = new JSONParser();
			pd = new ProgressDialog(MainActivity.this);
			pd.setCancelable(false);
			pd.setMessage("Searching...");
			grid.setVisibility(View.GONE);
			noresult.setVisibility(View.GONE);
			pd.getWindow().setGravity(Gravity.CENTER);
			pd.show();
		}

		@Override
		protected String doInBackground(String... sText) {

			url = "https://en.wikipedia.org/w/api.php?action=query&prop=pageimages&format=json&piprop=thumbnail&pithumbsize=50&pilimit=50&generator=prefixsearch&gpssearch="
					+ sText[0];
			String returnResult = getProductList(url);
			this.textSearch = sText[0];
			return returnResult;

		}

		public String getProductList(String url) {
			JSONParser jParser = new JSONParser();
			try {
				JSONObject json = jParser.getJSONFromUrl(url);
				if (json.has("query")) {
					Global.productList.clear();
					JSONObject query = json.getJSONObject("query");
					JSONObject pages = query.getJSONObject("pages");
					Iterator<String> keys = pages.keys();
					while (keys.hasNext()) {
						Product mProduct = new Product();
						String key = keys.next();
						Log.v("**********", "**********");
						Log.v("page key", key);
						JSONObject innerJObject = pages.getJSONObject(key);
						mProduct.setIndex(innerJObject.getInt("index"));
						mProduct.setNs(innerJObject.getInt("ns"));
						mProduct.setPage_id(innerJObject.getInt("pageid"));
						mProduct.setTitle(innerJObject.getString("title"));
						if (innerJObject.has("thumbnail")) {
							ArrayList<ThumbNail> thumbList = new ArrayList<>();
							Iterator<String> innerKeys = innerJObject
									.getJSONObject("thumbnail").keys();
							ThumbNail thumbNail = new ThumbNail();
							while (innerKeys.hasNext()) {
								String innerKkey = innerKeys.next();
								if (innerKkey.equalsIgnoreCase("source"))
									thumbNail.setSource(innerJObject
											.getJSONObject("thumbnail")
											.getString("source"));
								if (innerKkey.equalsIgnoreCase("height"))
									thumbNail.setHeight(innerJObject
											.getJSONObject("thumbnail").getInt(
													"height"));
								if (innerKkey.equalsIgnoreCase("width"))
									thumbNail.setWidth(innerJObject
											.getJSONObject("thumbnail").getInt(
													"width"));
							}
							thumbList.add(thumbNail);
							if (!thumbList.isEmpty()) {
								mProduct.setThumb_nail(thumbList);
							}
						}
						Global.productList.add(mProduct);
					}

				}
				return ("OK");

			} catch (Exception e) {
				e.printStackTrace();
				return ("Exception Caught");
			}
		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);

			if (result.equalsIgnoreCase("Exception Caught")) {
				Toast.makeText(MainActivity.this,
						"Unable to connect to server,please try later",
						Toast.LENGTH_LONG).show();

				pd.dismiss();
			} else {
				pd.dismiss();
				ArrayList<ThumbNail> toSetinAdapter = new ArrayList<ThumbNail>();
				for (int i = 0; i < Global.productList.size(); i++) {
					if (Global.productList.get(i).thumb_nail != null) {
						for (int j = 0; j < Global.productList.get(i).thumb_nail
								.size(); j++) {
							toSetinAdapter
									.add(Global.productList.get(i).thumb_nail
											.get(j));
						}
					}
				}
				if (!toSetinAdapter.isEmpty()) {
					grid.setVisibility(View.VISIBLE);
					noresult.setVisibility(View.GONE);
					CustomGridAdapter adapter = new CustomGridAdapter(
							MainActivity.this, toSetinAdapter);

					grid.setAdapter(adapter);
					
					 Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fly_in_from_center);
					 grid.setAnimation(anim);
                     anim.start();
				} else {
					grid.setVisibility(View.GONE);
					noresult.setVisibility(View.VISIBLE);
				}

				// calling this method to filter the search results from
				// productResults and move them to
				// //filteredProductResults
				// filterProductArray(textSearch);
				// searchResults.setAdapter(new
				// SearchResultsAdapter(MainActivity.this,filteredProductResults));
				// pd.dismiss();
			}
		}

	}
}
