package rs.os.messenger.common.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * 
 * @author zgavrilovic
 * 
 */
public class Preferences {
    private String id = "";
    private String server = "";
    private String port = "";
    private String servlet = "";

    public Preferences() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getServlet() {
        return servlet;
    }

    public void setServlet(String servlet) {
        this.servlet = servlet;
    }

    public static byte[] toBytes(Preferences settings) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeUTF(settings.getId());
            dos.writeUTF(settings.getServer());
            dos.writeUTF(settings.getPort());
            dos.writeUTF(settings.getServlet());
            return baos.toByteArray();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Preferences toObject(byte[] bytes) {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
        try {
            Preferences settings = new Preferences();
            settings.setId(dis.readUTF());
            settings.setServer(dis.readUTF());
            settings.setPort(dis.readUTF());
            settings.setServlet(dis.readUTF());
            return settings;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Preferences getDefaultPreferences() {
        Preferences settings = new Preferences();
        settings.setId("+381641924244");
        settings.setServer("192.168.1.100");
        settings.setPort("8081");
        settings.setServlet("/messenger/newmessage");
        return settings;
    }

    public String getServletUrl() {
        return "http://" + server + ':' + port + servlet;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Preferences other = (Preferences) obj;
        if ((this.id == null) ? (other.id != null) : !this.id.equals(other.id)) {
            return false;
        }
        if ((this.server == null) ? (other.server != null) : !this.server.equals(other.server)) {
            return false;
        }
        if ((this.port == null) ? (other.port != null) : !this.port.equals(other.port)) {
            return false;
        }
        if ((this.servlet == null) ? (other.servlet != null) : !this.servlet.equals(other.servlet)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 47 * hash + (this.server != null ? this.server.hashCode() : 0);
        hash = 47 * hash + (this.port != null ? this.port.hashCode() : 0);
        hash = 47 * hash + (this.servlet != null ? this.servlet.hashCode() : 0);
        return hash;
    }



    public static void main(String[] args) {
        Preferences settings = Preferences.getDefaultPreferences();

        byte[] bytes = Preferences.toBytes(settings);
        Preferences object = Preferences.toObject(bytes);

        boolean idEq = settings.getId().equals(object.getId());
        boolean serverEq = settings.getServer().equals(object.getServer());
        boolean portEq = settings.getPort().equals(object.getPort());
        boolean servletEq = settings.getServlet().equals(object.getServlet());
        boolean eq = idEq && serverEq && portEq && servletEq;

        System.out.println("Objects are equals " + eq);
    }
}