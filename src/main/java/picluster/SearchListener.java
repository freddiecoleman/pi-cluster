package picluster;

import java.util.EventListener;

public interface SearchListener extends EventListener {
    public void detailEventOccurred(SearchEvent event);
}