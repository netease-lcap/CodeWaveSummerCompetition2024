package oss_connector;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.netease.lowcode.core.annotation.NaslConnector;
import com.netease.lowcode.core.annotation.NaslLogic;

import java.io.File;

@NaslConnector(connectorKind = "ali_default")
public class Ali_oss {
    private String id;
    private String secret;
    private String endpoint;
    OSS ossclient;
    @NaslConnector.Creator
    public Ali_oss initBean(String id,String secret,String endpoint) {
        Ali_oss my_oss = new Ali_oss();
        my_oss.id = id;
        my_oss.secret = secret;
        my_oss.endpoint = endpoint;
        return my_oss;
    }
    @NaslConnector.Tester
    public Boolean test(String appKey) {
//        if (null != appKey && appKey.equals("myAppKey")) {
//            return true;
//        }
//        "不需要考虑验证"（并不知道应该怎么写）
        return true;
    }
    @NaslConnector.Logic
    public String create_bucket(String bucketname){
        Ali_oss my_oss = new Ali_oss().initBean(id,secret,endpoint);
        my_oss.ossclient = new OSSClientBuilder().build(
                my_oss.endpoint,
                my_oss.id,
                my_oss.secret);
        try {
            // 创建存储空间。
            my_oss.ossclient.createBucket(bucketname);
//            System.out.println("good job");
            return "Success";
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossclient != null) {
                ossclient.shutdown();
            }
        }
        return "fail";
    }
    @NaslConnector.Logic
    public String simple_up_file(String bucketName,String fpath,String objectName){
        Ali_oss my_oss = new Ali_oss().initBean(id,secret,endpoint);
        my_oss.ossclient = new OSSClientBuilder().build(
                my_oss.endpoint,
                my_oss.id,
                my_oss.secret);
        try {
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, new File(fpath));
            // 如果需要上传时设置存储类型和访问权限，请参考以下示例代码。
            // ObjectMetadata metadata = new ObjectMetadata();
            // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
            // metadata.setObjectAcl(CannedAccessControlList.Private);
            // putObjectRequest.setMetadata(metadata);

            // 上传文件。
            PutObjectResult result = my_oss.ossclient.putObject(putObjectRequest);
            return "success";
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (my_oss.ossclient != null) {
                my_oss.ossclient.shutdown();
            }
        }
        return "fail";
    }
    @NaslConnector.Logic
    public String delete_file(String bucketName,String objectName){
        Ali_oss my_oss = new Ali_oss().initBean(id,secret,endpoint);
        my_oss.ossclient = new OSSClientBuilder().build(
                my_oss.endpoint,
                my_oss.id,
                my_oss.secret);
        try {
            // 删除文件。
            my_oss.ossclient.deleteObject(bucketName, objectName);
            return "success";
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (my_oss.ossclient != null) {
                my_oss.ossclient.shutdown();
            }
        }
        return "fail";
    }

    /**
     已经实现需求：
        创建桶
        上传文件
        删除文件
     暂未实现：
        访问控制
        获取文件信息
        支持多家云服务
     */
///Users/nitro-blow/Desktop/瞬-clean.html
    public static void main(String[] args) {
//        MyConnector myConnector = new MyConnector().initBean("appKey");
//        alioss.test("appKey");
//        Ali_oss alioss = new Ali_oss().initBean();
//        alioss.create_bucket("conttt");
//        alioss.ossclient.createBucket("connctor-bk");
//        alioss.simple_up_file("aura-first-bucket","/Users/nitro-blow/Desktop/瞬-clean.html","efe");
//        System.out.println("add result :" + add);
//        myConnector.subscribe("queue1", new Function<String, String>() {
//            @Override
//            public String apply(String s) {
//                return null;
    }
}
