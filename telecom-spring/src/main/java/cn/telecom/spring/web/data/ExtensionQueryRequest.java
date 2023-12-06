package cn.telecom.spring.web.data;

import cn.telecom.commons.data.QueryRequest;
import cn.telecom.commons.data.UserSession;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;
import org.springframework.lang.Nullable;

@Getter
@Builder
public class ExtensionQueryRequest implements QueryRequest {

    public static final String KEY = "extension_query_request";

    private static final ExtensionQueryRequest EMPTY = new ExtensionQueryRequest();

    public static ExtensionQueryRequest empty() {
        return EMPTY;
    }

    private String uid;
    private String platformId;
    private String traceId;
    private String orgId;

    @Setter
    private @Nullable UserSession userSession;

    @Tolerate
    private ExtensionQueryRequest() {
    }


}
