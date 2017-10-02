package capital.fbg.wallet.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/12.
 * 功能描述：
 * 版本：@version
 */

public class OSSBean implements Serializable {

    private String RequestId;
    private AssumedRoleUserBean AssumedRoleUser;
    private CredentialsBean Credentials;

    public String getRequestId() {
        return RequestId;
    }

    public void setRequestId(String requestId) {
        RequestId = requestId;
    }

    public AssumedRoleUserBean getAssumedRoleUser() {
        return AssumedRoleUser;
    }

    public void setAssumedRoleUser(AssumedRoleUserBean assumedRoleUser) {
        AssumedRoleUser = assumedRoleUser;
    }

    public CredentialsBean getCredentials() {
        return Credentials;
    }

    public void setCredentials(CredentialsBean credentials) {
        Credentials = credentials;
    }

    public static class AssumedRoleUserBean implements Serializable{
        private String AssumedRoleId;
        private String Arn;

        public String getAssumedRoleId() {
            return AssumedRoleId;
        }

        public void setAssumedRoleId(String assumedRoleId) {
            AssumedRoleId = assumedRoleId;
        }

        public String getArn() {
            return Arn;
        }

        public void setArn(String arn) {
            Arn = arn;
        }
    }

    public static class CredentialsBean implements Serializable{
        private String AccessKeySecret;
        private String AccessKeyId;
        private String SecurityToken;
        private String Expiration;

        public String getAccessKeySecret() {
            return AccessKeySecret;
        }

        public void setAccessKeySecret(String accessKeySecret) {
            AccessKeySecret = accessKeySecret;
        }

        public String getAccessKeyId() {
            return AccessKeyId;
        }

        public void setAccessKeyId(String accessKeyId) {
            AccessKeyId = accessKeyId;
        }

        public String getSecurityToken() {
            return SecurityToken;
        }

        public void setSecurityToken(String securityToken) {
            SecurityToken = securityToken;
        }

        public String getExpiration() {
            return Expiration;
        }

        public void setExpiration(String expiration) {
            Expiration = expiration;
        }
    }
}
