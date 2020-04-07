/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package produk;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.result.UpdateResult;
import java.util.Date;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author Hamzah
 */
public class TestDB {
    public static void main(String[] args) {
        try {
            //koneksi ke Database MongoDB
            MongoDatabase database = Koneksi.sambungDB();
            
            //Melihat daftar Koleksi (tabel)
            System.out.println("=============================================");
            MongoIterable<String> tables = database.listCollectionNames();
            for (String name : tables) {
                System.out.println(name);
            }
            
            //Menambahkan data
            System.out.println("===============================================");
            System.out.println("Menabahkan Data");
            MongoCollection<Document> col = database.getCollection("produk");
            Document doc =  new Document();
            doc.put("nama", "Printer Injek");
            doc.put("harga", 1204000);
            doc.put("tanggal", new Date());
            col.insertOne(doc);
            System.out.println("Data telah disimpan dalam koleksi");
            
            //mendapatkan _id dari Dokumen yg baru saja di insert
            Object id =  new ObjectId(doc.get("_id").toString());
            
            //melihat/menapilkan data
            System.out.println("===============================================");
            System.out.println("Data Dalama koleksi  produk");
            MongoCursor<Document> cursor =  col.find().iterator();
            while (cursor.hasNext()){
                System.out.println(cursor.next().toJson());
            }
            
            //mencari Documnet berdasarkan ID
            Document myDoc =  col.find(eq("_id",id)).first();
            System.out.println("===============================================");
            System.out.println("pencarian data berdasarkan id: "+id);
            System.out.println(myDoc.toJson());
            
            //mengedit data
            Document docs =  new Document();
            docs.put("nama", "Canon");
            Document doc_edit = new  Document("$set", docs);
            UpdateResult hasil_edit =  col.updateOne(eq("_id",id), doc_edit);
            System.out.println(hasil_edit.getModifiedCount());
            
            //melihat/menapilkan data
            System.out.println("===============================================");
            System.out.println("Data Dalama koleksi  produk");
            cursor = col.find().iterator();
            while (cursor.hasNext()){
                System.out.println(cursor.next().toJson());
            }
            
            //mmeghapus data
            col.deleteOne(eq("_id", id));
            //melihat/menapilkan data
            System.out.println("===============================================");
            System.out.println("Data Dalama koleksi  produk");
            cursor = col.find().iterator();
            while (cursor.hasNext()){
                System.out.println(cursor.next().toJson());
            }
            
            
            
        } catch (Exception e) {
            System.err.println("error: "+e.getMessage());
        }
    }
}
