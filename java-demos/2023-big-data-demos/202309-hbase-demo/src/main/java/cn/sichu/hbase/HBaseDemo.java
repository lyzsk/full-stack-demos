package cn.sichu.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * Connection: 通过ConnectionFactory获取
 * Table: 负责DML操作
 * Admin: 负责DDL操作
 *
 * @author sichu
 * @date 2023/09/29
 **/
public class HBaseDemo {
    private static Connection connection;

    static {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "hadoop102,hadoop103,hadoop104");
        try {
            connection = ConnectionFactory.createConnection(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建 namespace
     *
     * @param namespace
     */
    public static void createNameSpace(String namespace) throws IOException {
        if (namespace == null || "".equals(namespace)) {
            System.err.println("namespace can't be null or empty");
            return;
        }
        // 获取 Admin 对象
        Admin admin = connection.getAdmin();
        NamespaceDescriptor.Builder builder = NamespaceDescriptor.create(namespace);
        NamespaceDescriptor namespaceDescriptor = builder.build();
        try {
            admin.createNamespace(namespaceDescriptor);
            System.out.println(namespace + " created successfully!");
        } catch (NamespaceExistException e) {
            System.err.println(namespace + " existed, failed to create");
        } finally {
            admin.close();
        }
    }

    /**
     * 创建 table
     *
     * @param nameSpaceName
     * @param tableName
     * @param cfs
     */
    public static void createTable(String nameSpaceName, String tableName, String... cfs) throws IOException {
        if (checkTableExist(nameSpaceName, tableName)) {
            System.err.println(
                (nameSpaceName == null || "".equals(nameSpaceName) ? "default" : nameSpaceName) + " : " + tableName
                    + " already existed");
            return;
        }
        Admin admin = connection.getAdmin();
        TableDescriptorBuilder tableDescriptorBuilder =
            TableDescriptorBuilder.newBuilder(TableName.valueOf(nameSpaceName, tableName));
        if (cfs == null || cfs.length < 1) {
            System.err.println("should have at least 1 column family");
            return;
        }
        for (String cf : cfs) {
            ColumnFamilyDescriptorBuilder columnFamilyDescriptorBuilder =
                ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(cf));
            ColumnFamilyDescriptor columnFamilyDescriptor = columnFamilyDescriptorBuilder.build();
            tableDescriptorBuilder.setColumnFamily(columnFamilyDescriptor);
        }
        TableDescriptor tableDescriptor = tableDescriptorBuilder.build();
        admin.createTable(tableDescriptor);
        admin.close();
    }

    public static boolean checkTableExist(String nameSpaceName, String tableName) throws IOException {
        Admin admin = connection.getAdmin();
        return admin.tableExists(TableName.valueOf(nameSpaceName, tableName));
    }

    /**
     * 删除 table
     *
     * @param nameSpaceName
     * @param tableName
     * @throws IOException
     */
    public static void deleteTable(String nameSpaceName, String tableName) throws IOException {
        if (!checkTableExist(nameSpaceName, tableName)) {
            System.err.println("table not exist");
            return;
        }
        Admin admin = connection.getAdmin();
        TableName tblname = TableName.valueOf(nameSpaceName, tableName);
        admin.disableTable(tblname);
        admin.deleteTable(tblname);
        admin.close();
    }

    /**
     * DML put data into table
     *
     * @param nameSpaceName
     * @param tableName
     * @param rowkey
     * @param cf
     * @param cl
     * @param value
     */
    public static void putData(String nameSpaceName, String tableName, String rowkey, String cf, String cl,
        String value) throws IOException {
        Table table = connection.getTable(TableName.valueOf(nameSpaceName, tableName));
        Put put = new Put(Bytes.toBytes(rowkey));
        put.addColumn(Bytes.toBytes(cf), Bytes.toBytes(cl), Bytes.toBytes(value));
        table.put(put);
        table.close();
    }

    /**
     * DML 删除
     *
     * @param nameSpaceName
     * @param tableName
     * @param rowkey
     * @param cf
     * @param cl
     */
    public static void deleteData(String nameSpaceName, String tableName, String rowkey, String cf, String cl)
        throws IOException {
        Table table = connection.getTable(TableName.valueOf(nameSpaceName, tableName));
        Delete delete = new Delete(Bytes.toBytes(rowkey));
        // type=DeleteFamily
        // delete.addFamily(Bytes.toBytes(cf));

        // type=Delete
        // delete.addColumn(Bytes.toBytes(cf), Bytes.toBytes(cl));

        // type=DeleteColumn
        delete.addColumns(Bytes.toBytes(cf), Bytes.toBytes(cl));
        table.delete(delete);
        table.close();
    }

    /**
     * DML get data
     *
     * @param nameSpaceName
     * @param tableName
     * @param rowkey
     * @param cf
     * @param cl
     */
    public static void getData(String nameSpaceName, String tableName, String rowkey, String cf, String cl)
        throws IOException {
        Table table = connection.getTable(TableName.valueOf(nameSpaceName, tableName));
        Get get = new Get(Bytes.toBytes(rowkey));
        // get.addFamily(Bytes.toBytes(cf));
        get.addColumn(Bytes.toBytes(cf), Bytes.toBytes(cl));
        Result result = table.get(get);
        Cell[] cells = result.rawCells();
        for (Cell cell : cells) {
            String cellStr =
                Bytes.toString(CellUtil.cloneRow(cell)) + " : " + Bytes.toString(CellUtil.cloneFamily(cell)) + " : "
                    + Bytes.toString(CellUtil.cloneQualifier(cell)) + " : " + Bytes.toString(CellUtil.cloneValue(cell));
            System.out.println(cellStr);
        }
        table.close();
    }

    public static void main(String[] args) throws IOException {
        // createNameSpace("mydb2");
        // createTable("", "t1", "info1", "info2");
        // deleteTable("", "t1");
        // putData("", "stu", "2023", "info", "name", "shabi");
        // deleteData("", "stu", "1002", "info", "");
        // deleteData("", "stu", "1003", "info", "name");
        // deleteData("", "stu", "1003", "info", "sex");
        // getData("", "stu", "1003$", "", "");
        // getData("", "stu", "1001", "info", "");
        getData("", "stu", "1001", "msg", "salary");
    }
}
