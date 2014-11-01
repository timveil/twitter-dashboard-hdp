package dashboard.view.service;

import dashboard.view.model.Configuration;
import dashboard.view.model.IngestStatus;

public interface TweetStreamService {

    IngestStatus startIngest(Configuration configuration);

    IngestStatus stopIngest();

    IngestStatus ingestStatus();
}
