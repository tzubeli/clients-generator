package com.kaltura.client.utils.request;

import java.util.HashMap;

import com.kaltura.client.Client;
import com.kaltura.client.Configuration;
import com.kaltura.client.FileHolder;
import com.kaltura.client.Files;
import com.kaltura.client.Params;
import com.kaltura.client.types.APIException;
import com.kaltura.client.utils.APIConstants;
import com.kaltura.client.utils.EncryptionUtils;
import com.kaltura.client.utils.GsonParser;
import com.kaltura.client.utils.response.OnCompletion;
import com.kaltura.client.utils.response.base.ResponseElement;

/**
 * Created by tehilarozin on 14/08/2016.
 */
public abstract class BaseRequestBuilder<T> implements RequestElement {

	private Class<T> type;
    protected String id;
    protected String url;
    protected Params params;
    protected Files files;
    protected HashMap<String, String> headers;
    private ConnectionConfiguration connectionConfig;

    /**
     * callback for the parsed response.
     */
    protected OnCompletion<T> onCompletion;



    protected BaseRequestBuilder(Class<T> type) {
    	this.type = type;
        this.params = new Params();
    }

    protected BaseRequestBuilder(Class<T> type, Params params) {
    	this.type = type;
        this.params = params;
    }

    protected BaseRequestBuilder(Class<T> type, Params params, Files files) {
    	this(type, params);
    }

    protected BaseRequestBuilder(Params params) {
        this.params = params;
    }

    protected BaseRequestBuilder(Params params, Files files) {
        this(params);
        this.files = files;
    }


    public abstract String getUrlTail();

    public abstract String getTag();


    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public String getBody() {
        return params.toString();
    }

    @Override
    public String getId() {
        return id;
    }

	public Params getParams() {
        return params;
    }
	
    public void setParams(Object objParams) {
        params.putAll((Params) objParams); // !! null params should be checked - should not appear in request body or be presented as empty string.
	}

    public BaseRequestBuilder<T> setFile(String key, FileHolder value) {
        if (files != null) {
            files.add(key, value);
        }
        return this;
    }

    @Override
    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public void setHeaders(String ... nameValueHeaders){
        for (int i = 0 ; i < nameValueHeaders.length-1 ; i+=2){
            this.headers.put(nameValueHeaders[i], nameValueHeaders[i+1]);
        }
    }

    @Override
    public String getContentType() {
        return headers != null ? headers.get(APIConstants.HeaderContentType) : APIConstants.DefaultContentType;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public ConnectionConfiguration config() {
        return connectionConfig;
    }

    /**
     * Builds the final list of parameters including the default params and the configured params.
     *
     * @param configurations
     * @param addSignature
     * @return
     */
    protected Params prepareParams(Client configurations, boolean addSignature) {

        if(params == null){
            params = new Params();
        }

        // add default params:
        //params.add("format", configurations.getConnectionConfiguration().getServiceResponseTypeFormat());
        params.add("ignoreNull", true);
        if(configurations != null) {
            params.putAll(configurations.getClientConfiguration());
            params.putAll(configurations.getRequestConfiguration());
        }
        if (addSignature) {
            params.add("kalsig", EncryptionUtils.encryptMD5(params.toString()));
        }
        return params;
    }

    private void prepareUrl(String endPoint) {
        if (url == null) {
            StringBuilder urlBuilder = new StringBuilder(endPoint)
                    .append(APIConstants.UrlApiVersion);

            urlBuilder.append(getUrlTail());
            url = urlBuilder.toString();
        }
    }

    public RequestElement build(final Client client) {
        return build(client, false);
    }

    @SuppressWarnings("unchecked")
	@Override
    public void onComplete(ResponseElement response) {
        T result = null;
        APIException error = null;
        
        if(!response.isSuccess()) {
        	error = generateErrorResponse(response);
        } else {
        	try {
				result = (T) parse(response.getResponse());
			} catch (APIException e) {
	        	error = e;
			}
        }

        if(onCompletion != null) {
        	onCompletion.onComplete(result, error);
        }
    }
    
    protected Object parse(String response) throws APIException {
    	return GsonParser.parseObject(response, type);
    }
    
    protected APIException generateErrorResponse(ResponseElement response) {
    	return GsonParser.parseException(response.getResponse());
    }
    
    public RequestElement build(final Client client, boolean addSignature) {
        connectionConfig = client != null ? client.getConnectionConfiguration() : Configuration.getDefaults();

        prepareParams(client, addSignature);
        prepareHeaders(connectionConfig);
        prepareUrl(connectionConfig.getEndpoint());

        return this;
    }

    private void prepareHeaders(ConnectionConfiguration config) {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        addDefaultHeaders();

        if (!headers.containsKey(APIConstants.HeaderAcceptEncoding) && config.getAcceptGzipEncoding()) {
            headers.put(APIConstants.HeaderAcceptEncoding, APIConstants.HeaderEncodingGzip);
        }
    }

    private void addDefaultHeaders() {
        if(!headers.containsKey(APIConstants.HeaderAccept)) {
            headers.put(APIConstants.HeaderAccept, "application/json");
        }
        if(!headers.containsKey(APIConstants.HeaderAcceptCharset)) {
            headers.put(APIConstants.HeaderAcceptCharset, "utf-8,ISO-8859-1;q=0.7,*;q=0.5");
        }
    }


}














