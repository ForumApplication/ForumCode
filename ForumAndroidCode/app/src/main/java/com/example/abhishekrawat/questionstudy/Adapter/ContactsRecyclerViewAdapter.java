package com.example.abhishekrawat.questionstudy.Adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhishekrawat.questionstudy.Model.AnswerDTO;
import com.example.abhishekrawat.questionstudy.Model.ContactsDTO;
import com.example.abhishekrawat.questionstudy.R;
import com.example.abhishekrawat.questionstudy.ui.ContactsReadFragment;

import java.util.ArrayList;
import java.util.List;

public class ContactsRecyclerViewAdapter extends RecyclerView.Adapter<ContactsRecyclerViewAdapter.ContactsViewHolder>
    implements View.OnClickListener{

    List<ContactsDTO> contactsList;
    private Filter filter;
    Context mContext;
    ContactAdapterListener mListener;
    public ContactsRecyclerViewAdapter(Context context, List<ContactsDTO> contacts) {
        this.mContext = context;
        this.contactsList = contacts;
    }

    public void setItems(List<ContactsDTO> contacts) {
        this.contactsList = contacts;
    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        ContactsRecyclerViewAdapter.ContactsViewHolder vh = new ContactsRecyclerViewAdapter.ContactsViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {
        ContactsDTO contact = contactsList.get(position);
        holder.contactName.setText(contact.name);
        holder.contactNumber.setText(contact.mobile);
        holder.addBtn.setOnClickListener(this);
        holder.addBtn.setTag(contact);
        mListener=(ContactAdapterListener) mContext;

    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.add_btn:
                ImageView img=(ImageView)v;
                img.setImageResource(R.drawable.bg_circle_green_tick);
                mListener.addContact((ContactsDTO)v.getTag());
                break;
        }
    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder {
        TextView contactName, contactNumber;
        ImageView addBtn;

        public ContactsViewHolder(View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contact_name);
            addBtn = itemView.findViewById(R.id.add_btn);
            contactNumber = itemView.findViewById(R.id.contact_number);
        }
    }

    public Filter getFilter() {
        if (filter == null)
            filter = new AppFilter<ContactsDTO>(contactsList);
        return filter;
    }
    public interface ContactAdapterListener{
        void addContact(ContactsDTO contact);
    }

    /**
     * Class for filtering in Arraylist listview. Objects need a valid
     * 'toString()' method.
     *
     * @author Tobias Schürg inspired by Alxandr
     * (http://stackoverflow.com/a/2726348/570168)
     */
    private class AppFilter<T> extends Filter {
        private ArrayList<T> sourceObjects;

        public AppFilter(List<T> objects) {
            sourceObjects = new ArrayList<>();
            synchronized (this) {
                sourceObjects.addAll(objects);
            }
        }

        @Override
        protected FilterResults performFiltering(CharSequence chars) {
            String filterSeq = chars.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (filterSeq != null && filterSeq.length() > 0) {
                ArrayList<ContactsDTO> filter = new ArrayList<ContactsDTO>();

                for (ContactsDTO object : (List<ContactsDTO>) sourceObjects) {
                    // the filtering itself:
                    if (object.name.toString().toLowerCase().contains(filterSeq) ||
                            object.mobile.toString().toLowerCase().contains(filterSeq))
                        filter.add(object);
                }
                result.count = filter.size();
                result.values = filter;
            } else {
                // add all objects
                synchronized (this) {
                    result.values = sourceObjects;
                    result.count = sourceObjects.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            // NOTE: this function is *always* called from the UI thread.
            ArrayList<T> filtered = (ArrayList<T>) results.values;
            notifyDataSetChanged();
            contactsList.clear();
            for (int i = 0, l = filtered.size(); i < l; i++)
                contactsList.add((ContactsDTO) filtered.get(i));
        }
    }

}
