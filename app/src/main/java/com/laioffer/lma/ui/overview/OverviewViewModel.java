package com.laioffer.lma.ui.overview;

import androidx.lifecycle.LiveData;
import com.laioffer.lma.ui.overview.Machine.MachineBuilder;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.Mac;

public class OverviewViewModel extends ViewModel {

    /*
    private MutableLiveData<List<Machine>> machineData;
    public OverviewViewModel() {
        machineData = new MutableLiveData<>();
        List<Machine> list = getMachines();
        machineData.setValue(list);
    }
*/
    private MutableLiveData<String> mText;

    public OverviewViewModel() {
        mText = new MutableLiveData<>();
        List<Machine> list = createMachines();
        int totalNum = list.size();
        int num = countAvailableMachines(list);
        mText.setValue("The number of available machines are " + num + " out of " + totalNum);
    }

    private int countAvailableMachines(List<Machine> list) {
        int count = 0;
        for (Machine m : list) {
            if (m.getIsAvailable() == true) {
                count++;
            }
        }
        return count;
    }
    private List<Machine> createMachines() {
        List<Machine> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            MachineBuilder builder = new MachineBuilder();
            builder.setId("machine" + i);
            builder.setIsAvailable(true);
            builder.setlocationID(i + "_" + i);
            Machine machine = builder.build();
            list.add(machine);
        }
        return list;
    }

    public LiveData<String> getText() {
        return mText;
    }
    /*
    private MutableLiveData<String> mText;

    public OverviewViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }*/
}