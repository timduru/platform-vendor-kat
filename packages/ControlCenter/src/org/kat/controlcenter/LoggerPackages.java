
package org.kat.controlcenter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LoggerPackages extends Fragment {

    public static LoggerPackages newInstance(Bundle args) {
        LoggerPackages frag = new LoggerPackages();
        if (args != null) {
            args.putString(Utils.FRAG_TITLE_KEY, "Logger Packages");
        }
        frag.setArguments(args);
        return frag;
    }

    public static LoggerPackages newInstance() {
        LoggerPackages frag = new LoggerPackages();
        Bundle args = new Bundle();
        args.putString(Utils.FRAG_TITLE_KEY, "Logger Packages");
        frag.setArguments(args);
        return frag;
    }

    public LoggerPackages(Bundle args) {
        newInstance(args);
    }

    public LoggerPackages() {
    }

    private static final String DEFAULT_ADD_ACTION = "";

    private ButtonOnClickListener mButtonOnClickListener;
    private ListView mActionsListView;
    private ArrayList<String> mListPackages;
    private ActionsAdapter mListPackagesAdapter;
    private ArrayList<String> mPackages;
    private ArrayList<String> mPackageUids;
    private int mPackagesCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mButtonOnClickListener = new ButtonOnClickListener();
        mListPackages = new ArrayList<String>();
        mListPackagesAdapter = new ActionsAdapter(getActivity(),
                R.layout.privacy_logger_packages,
                R.id.eos_action_text, mListPackages);

        try {
            mPackages = new ArrayList<String>();
            mPackageUids = new ArrayList<String>();

            SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setContentHandler(new PackageXmlHandler());
            xmlReader.parse(new InputSource(new FileReader("/data/system/packages.xml")));
        } catch (Exception e) {
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(getActivity()
                    .getDir("eos", Context.MODE_PRIVATE), Privacy.UIDS_LIST_FILE)));
            String input = null;
            while ((input = reader.readLine()) != null) {
                if (input.equals(""))
                    continue;
                if (mPackageUids != null) {
                    int index = mPackageUids.indexOf(input);
                    if (index > -1) {
                        mListPackages.add(mPackages.get(index));
                    }
                }
            }
            reader.close();
        } catch (IOException e) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.logger_packages, container, false);
        ((Button) v.findViewById(R.id.eos_lockscreen_actions_add))
                .setOnClickListener(mButtonOnClickListener);
        ((Button) v.findViewById(R.id.eos_lockscreen_actions_save))
                .setOnClickListener(mButtonOnClickListener);
        mActionsListView = (ListView) v.findViewById(R.id.eos_lockscreen_actions_list);
        mActionsListView.setAdapter(mListPackagesAdapter);
        return v;
    }

    private class ButtonOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.eos_lockscreen_actions_save:
                    BufferedWriter writer;
                    try {
                        writer = new BufferedWriter(new FileWriter(new File(
                                getActivity().getDir("eos", Context.MODE_PRIVATE),
                                Privacy.UIDS_LIST_FILE)));
                        for (String packageName : mListPackages) {
                            if (packageName.equals(""))
                                continue;
                            int index = mPackages.indexOf(packageName);
                            writer.write(mPackageUids.get(index) + "\n");
                        }
                        writer.close();
                    } catch (IOException e) {
                    }
                    return;
                case R.id.eos_lockscreen_actions_add:
                    if (mListPackages.size() <= 100) {
                        mListPackagesAdapter.add(DEFAULT_ADD_ACTION);
                    } else {
                        Toast toast = Toast.makeText(getActivity(),
                                "Cannot have more than 100 packages in the list.",
                                Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    return;
            }
        }

    }

    private class ActionsAdapter extends ArrayAdapter<String> {
        private Activity mContext;
        private int mLayoutId;
        private int mTextViewResourcesId;
        private List<String> mSequences;
        private PackageManager mPackageManager;

        public ActionsAdapter(Activity context, int layout, int textViewResourceId,
                List<String> objects) {
            super(context, textViewResourceId, objects);
            mContext = context;
            mLayoutId = layout;
            mTextViewResourcesId = textViewResourceId;
            mSequences = objects;
            mPackageManager = mContext.getPackageManager();
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            View newView = null;
            if (convertView != null)
                newView = convertView;
            else {
                newView = ((LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(mLayoutId, parent, false);
            }

            ((Button) newView.findViewById(R.id.eos_action_remove))
                    .setOnClickListener(new OnRemoveClick(position));
            ((Button) newView.findViewById(R.id.eos_action_add))
                    .setOnClickListener(new OnAddClick(position));
            newView.setOnClickListener(new OnItemClick(position));

            String packageName = getItem(position);
            if (packageName == null || packageName.equals(""))
                packageName = "Select a package...";

            ((TextView) newView.findViewById(mTextViewResourcesId)).setText(packageName);
            return newView;
        }

        private class OnRemoveClick implements View.OnClickListener {
            private int mPosition;

            public OnRemoveClick(int position) {
                mPosition = position;
            }

            @Override
            public void onClick(View v) {
                mSequences.remove(mPosition);
                notifyDataSetChanged();
            }

        }

        private class OnAddClick implements View.OnClickListener {
            private int mPosition;

            public OnAddClick(int position) {
                mPosition = position;
            }

            @Override
            public void onClick(View v) {
                if (mListPackages.size() <= 100) {
                    mSequences.add(mPosition, DEFAULT_ADD_ACTION);
                } else {
                    Toast toast = Toast.makeText(getActivity(),
                            "Cannot have more than 100 packages in the list.",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
                notifyDataSetChanged();
            }
        }

        private class OnItemClick implements View.OnClickListener {
            private int mPosition;

            public OnItemClick(int position) {
                mPosition = position;
            }

            @Override
            public void onClick(View v) {
                String content = getItem(mPosition);
                int index = -1;
                if (content == null || content.equals("") || content.equals("null")) {
                    index = -1;
                } else {
                    index = mPackages.indexOf(content);
                }

                new AlertDialog.Builder(getActivity())
                        .setTitle("Select application package")
                        .setSingleChoiceItems(mPackages.toArray(new String[0]), index,
                                new Dialog.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mSequences.remove(mPosition);
                                        mSequences.add(mPosition, mPackages.get(which));
                                        notifyDataSetChanged();
                                        dialog.dismiss();
                                    }
                                })
                        .create().show();
            }
        }
    }

    private class PackageXmlHandler extends DefaultHandler {
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes)
                throws SAXException {
            if (localName.equals("package")) {
                String name = attributes.getValue("name");
                String userId = attributes.getValue("userId");
                if (name != null && userId != null) {
                    mPackages.add(name);
                    mPackageUids.add(userId);
                }
            }
        }
    }
}
