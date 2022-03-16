import java.util.Date;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Comentario implements Registro {
    private int ID;
    private String authorName;
    private String title;
    private String body;
    private Date date;

    public Comentario() {
    }

    public Comentario(int ID, String authorName, String title, String body) {
        this.ID = ID;
        this.authorName = authorName;
        this.title = title;
        this.body = body;
        this.date = new Date();
    }

    public int getID() {
        return this.ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.ID);
        dos.writeUTF(this.authorName);
        dos.writeUTF(this.title);
        dos.writeUTF(this.body);
        dos.writeLong(this.date.getTime()); // write millis

        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.ID = dis.readInt();
        this.authorName = dis.readUTF();
        this.title = dis.readUTF();
        this.body = dis.readUTF();
        this.date = new Date(dis.readLong());
    }

    public String toString(){
        return ("==== Comentário ====\n" + 
                "ID: "       + this.ID + "\n" + 
                "Autor: "    + this.authorName + "\n" +
                "Título: "   + this.title + "\n" + 
                "Comentou: " + this.body + "\n" + 
                "Data: "     + this.date + "\n"
                );
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}