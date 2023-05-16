git pull
mvn clean install
mv -f /data/teyue/admin.jar /data/nenyiprj/admin_bak.jar
cp /data/code/teyue/ruoyi-admin/target/admin.jar /data/teyue/admin.jar
mv -f /data/teyue/api.jar /data/teyue/api.jar
cp /data/code/teyue/ruoyi-sifang/target/api.jar /data/teyue/api.jar
sh /data/teyue/run-admin-jk.sh
sh /data/teyue/run-api-jk.sh
