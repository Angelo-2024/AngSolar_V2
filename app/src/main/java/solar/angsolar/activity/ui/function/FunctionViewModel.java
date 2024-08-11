package solar.angsolar.activity.ui.function;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FunctionViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public FunctionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is function fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }


}
