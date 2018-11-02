package ru.scapegoats.truereader.modules;


public interface Presenter<T extends Viewable> {
    void onAttach(T view);

    void onDetach();
}
