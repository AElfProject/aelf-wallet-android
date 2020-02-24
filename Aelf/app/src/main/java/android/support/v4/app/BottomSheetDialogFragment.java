package android.support.v4.app;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;

public class BottomSheetDialogFragment extends AppCompatDialogFragment {
    public BottomSheetDialogFragment() {
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(this.getContext(), this.getTheme());
    }
}