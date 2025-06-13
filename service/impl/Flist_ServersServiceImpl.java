package egovframework.com.fivemlist.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.net.ssl.SSLContext;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import java.security.cert.X509Certificate;
import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.com.fivemlist.service.Flist_ServersService;
import egovframework.com.fivemlist.service.ServerVO;
import egovframework.com.fivemlist.service.serverInfoVO;
import egovframework.com.fivemlist.service.serverdataVO;

@Service("Flist_ServersService")
public class Flist_ServersServiceImpl implements Flist_ServersService {
	
	@Resource(name = "Flist_ServersDAO")
	private Flist_ServersDAO serverdao;

	@SuppressWarnings("null")
	@Override
	public List<ServerVO> getServers() {
		List<ServerVO> rawservers = serverdao.getServers();
		
		
		List<ServerVO> servers = new ArrayList<>();
		for (ServerVO server: rawservers) {
			servers.add(getServerInfo(server.getEndpoint()));
			

	        
		}
		return servers;
	}
	public static CloseableHttpClient createUnsafeHttpClient() throws Exception {
        // 모든 인증서를 신뢰하는 전략
        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        // SSLContext 생성
        SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();

        // 인증서 무시 및 호스트네임 무시 설정
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

        return HttpClients.custom()
                .setSSLSocketFactory(csf)
                .build();
    }

	@Override
	public ServerVO getServerInfo(String endpoint) {
		String url = "https://servers-frontend.fivem.net/api/servers/single/"+endpoint;
		try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = client.execute(request)) {
                String jsonResponse = EntityUtils.toString(response.getEntity());
                
                ObjectMapper mapper = new ObjectMapper();
                ServerVO serverData = mapper.readValue(jsonResponse, ServerVO.class);
                
                serverData.setDirectAddress(serverData.getData().getConnectEndPoints().get(0));
                url = "https://"+serverData.getDirectAddress()+"/info.json";
        		
        		try (CloseableHttpClient client1 = createUnsafeHttpClient()) {
                    HttpGet request1 = new HttpGet(url);
                    try (CloseableHttpResponse response1 = client1.execute(request1)) {
                        String jsonResponse1 = EntityUtils.toString(response1.getEntity());
                        
                        ObjectMapper mapper1 = new ObjectMapper();
                        serverInfoVO serverData1 = mapper1.readValue(jsonResponse1, serverInfoVO.class);
                        
                        serverData.setInfo(serverData1);
                        System.out.println(serverData.getDirectAddress());
                        System.out.println(serverData1.getIcon());
                        return serverData;
                    }
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		
		
		
		return null;
	}
	
}
