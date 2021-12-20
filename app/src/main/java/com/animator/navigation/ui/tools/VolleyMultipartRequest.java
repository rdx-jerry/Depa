package com.animator.navigation.ui.tools;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;


@SuppressWarnings("unused")
public class VolleyMultipartRequest extends Request<JSONObject> {
    private final String boundary = ("apiclient-" + System.currentTimeMillis());
    private final String lineEnd = "\r\n";
    private Response.ErrorListener mErrorListener;
    private Map<String, String> mHeaders;
    private Response.Listener<JSONObject> mListener;
    private final String twoHyphens = "--";

    public VolleyMultipartRequest(String url, Map<String, String> headers, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(1, url, errorListener);
        this.mListener = listener;
        this.mErrorListener = errorListener;
        this.mHeaders = headers;
    }

    public VolleyMultipartRequest(int method, String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mListener = listener;
        this.mErrorListener = errorListener;
    }

    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> map = this.mHeaders;
        return map != null ? map : super.getHeaders();
    }

    public String getBodyContentType() {
        return "multipart/form-data;boundary=" + this.boundary;
    }

    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            Map<String, String> params = getParams();
            if (params != null && params.size() > 0) {
                textParse(dos, params, getParamsEncoding());
            }
            Map<String, ArrayList<DataPart>> data = getByteData();
            if (data != null && data.size() > 0) {
                dataParse(dos, data);
            }
            dos.writeBytes("--" + this.boundary + "--" + "\r\n");
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public Map<String, ArrayList<DataPart>> getByteData() throws AuthFailureError {
        return null;
    }

    /* access modifiers changed from: protected */
    public Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            return Response.success(new JSONObject(new String(response.data, HttpHeaderParser.parseCharset(response.headers))), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError((Throwable) e));
        } catch (JSONException je) {
            return Response.error(new ParseError((Throwable) je));
        }
    }

    /* access modifiers changed from: protected */
    public void deliverResponse(JSONObject response) {
        this.mListener.onResponse(response);
    }

    public void deliverError(VolleyError error) {
        this.mErrorListener.onErrorResponse(error);
    }

    private void textParse(DataOutputStream dataOutputStream, Map<String, String> params, String encoding) throws IOException {
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                buildTextPart(dataOutputStream, entry.getKey(), entry.getValue());
            }
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + encoding, uee);
        }
    }

    private void dataParse(DataOutputStream dataOutputStream, Map<String, ArrayList<DataPart>> data) throws IOException {
        for (Map.Entry<String, ArrayList<DataPart>> entry : data.entrySet()) {
            buildDataPart(dataOutputStream, entry.getValue(), entry.getKey());
        }
    }

    private void buildTextPart(DataOutputStream dataOutputStream, String parameterName, String parameterValue) throws IOException {
        dataOutputStream.writeBytes("--" + this.boundary + "\r\n");
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + parameterName + "\"" + "\r\n");
        dataOutputStream.writeBytes("\r\n");
        StringBuilder sb = new StringBuilder();
        sb.append(parameterValue);
        sb.append("\r\n");
        dataOutputStream.writeBytes(sb.toString());
    }

    private void buildDataPart(DataOutputStream dataOutputStream, ArrayList<DataPart> dataFile, String inputName) throws IOException {
        for (int i = 0; i < dataFile.size(); i++) {
            dataOutputStream.writeBytes("--" + this.boundary + "\r\n");
            DataPart dp = dataFile.get(i);
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + dp.getFileName() + "\"" + "\r\n");
            if (dp.getType() != null && !dp.getType().trim().isEmpty()) {
                dataOutputStream.writeBytes("Content-Type: " + dp.getType() + "\r\n");
            }
            dataOutputStream.writeBytes("\r\n");
            ByteArrayInputStream fileInputStream = new ByteArrayInputStream(dp.getContent());
            int bufferSize = Math.min(fileInputStream.available(), 1048576);
            byte[] buffer = new byte[bufferSize];
            int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                dataOutputStream.write(buffer, 0, bufferSize);
                bufferSize = Math.min(fileInputStream.available(), 1048576);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            dataOutputStream.writeBytes("\r\n");
        }
    }

    public static class DataPart {
        private byte[] content;
        private String fileName;
        private String type;

        public DataPart() {
        }

        public DataPart(String name, byte[] data) {
            this.fileName = name;
            this.content = data;
        }

        public DataPart(String name, byte[] data, String mimeType) {
            this.fileName = name;
            this.content = data;
            this.type = mimeType;
        }

        public String getFileName() {
            return this.fileName;
        }

        public void setFileName(String fileName2) {
            this.fileName = fileName2;
        }

        public byte[] getContent() {
            return this.content;
        }

        public void setContent(byte[] content2) {
            this.content = content2;
        }

        public String getType() {
            return this.type;
        }

        public void setType(String type2) {
            this.type = type2;
        }
    }
}