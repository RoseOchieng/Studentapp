@Override
protected void onCreate(Bundle s) {
    super.onCreate(s);
    onCreate(R.layout.activity_settings);  // connects layout
    ...
    findViewById(R.id.btn_save).setOnClickListener(v -> {
        ...
    });
}