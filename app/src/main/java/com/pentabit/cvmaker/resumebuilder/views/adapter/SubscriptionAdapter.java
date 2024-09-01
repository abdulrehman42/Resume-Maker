package com.pentabit.cvmaker.resumebuilder.views.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.pentabit.cvmaker.resumebuilder.R;
import com.pentabit.cvmaker.resumebuilder.application.ResumeMakerApplication;
import com.pentabit.cvmaker.resumebuilder.databinding.StoragePackageListItemBinding;
import com.pentabit.cvmaker.resumebuilder.models.SubscriptionPackageModel;
import com.pentabit.pentabitessentials.views.AppsKitSDKRecyclerBaseViewBinding;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class SubscriptionAdapter extends ListAdapter<SubscriptionPackageModel, AppsKitSDKRecyclerBaseViewBinding> {

    LinearLayout currentLayout;
    Map<Integer, LinearLayout> itemsLst = new HashMap<>();

    public SubscriptionAdapter() {
        super(new StorageSubscriptionPackageDiffUtils());
    }

    @NonNull
    @Override
    public AppsKitSDKRecyclerBaseViewBinding onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AppsKitSDKRecyclerBaseViewBinding(StoragePackageListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AppsKitSDKRecyclerBaseViewBinding holder, int position) {
        StoragePackageListItemBinding binding = (StoragePackageListItemBinding) holder.binding;
        SubscriptionPackageModel model = getItem(holder.getAbsoluteAdapterPosition());
        itemsLst.put(holder.getAbsoluteAdapterPosition(), binding.mainContainer);
        binding.name.setText(model.getName());
        if (model.getDiscount().equals("0")) {
            binding.discount.setVisibility(View.GONE);
        } else {
            binding.discount.setText(MessageFormat.format("{0} % Discount", model.getDiscount()));
        }
        if (!model.isFreeTrial()) {
            binding.trial.setVisibility(View.GONE);
        }
        binding.price.setText(model.getPrice());
        binding.timePeriod.setText(model.getTimePeriod());
        if (currentLayout == null && holder.getAbsoluteAdapterPosition() == 1) {
            updateLayoutAsSelected(1);
        }
    }

    public void updateLayoutAsSelected(int pos) {
        if (itemsLst.get(pos) != null)
            manageUIUpdate(itemsLst.get(pos));
    }


    private void manageUIUpdate(LinearLayout view) {
        if (currentLayout != null)
            currentLayout.setBackground(ContextCompat.getDrawable(ResumeMakerApplication.Companion.getInstance(), R.drawable.unselected_storage_subscription_bg));
        currentLayout = view;
        currentLayout.setBackground(ContextCompat.getDrawable(ResumeMakerApplication.Companion.getInstance(), R.drawable.selected_storage_subscription_bg));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class StorageSubscriptionPackageDiffUtils extends DiffUtil.ItemCallback<SubscriptionPackageModel> {
        @Override
        public boolean areItemsTheSame(@NonNull SubscriptionPackageModel oldItem, @NonNull SubscriptionPackageModel newItem) {
            return oldItem.hashCode() == newItem.hashCode();
        }

        @Override
        public boolean areContentsTheSame(@NonNull SubscriptionPackageModel oldItem, @NonNull SubscriptionPackageModel newItem) {
            return oldItem.toString().equals(newItem.toString());
        }
    }

}
