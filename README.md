# IPC_FileShare
Android开发艺术探索，脑图笔记 part 2 文件共享项目Demo 

### IPC文件共享

通过对象文件共享数据 ，采用对象流进行存储`ObjectOutputStream` and `ObjectInputStream` 这两个对象来进行文件操作 。

### 将对象数据写入到文件中

``` java 

			// 将对象写入文件
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(new File(externalStorageDirectory,"cache"))) ;
            outputStream.writeObject(p);

            outputStream.close();

```

### 将对象数据从文件中读取出来

``` java

			// 读取对象文件
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            ObjectInputStream inputStream = new ObjectInputStream(new BufferedInputStream(
                    new FileInputStream(new File(externalStorageDirectory, "cache"))));
            Person person = (Person) inputStream.readObject();

            inputStream.close();

````

## SharedPreferences文件进行数据共享

### 将数据存储到本进程SharedPreferences文件中

``` java 

		SharedPreferences sp = getSharedPreferences("sp",
                Context.MODE_WORLD_WRITEABLE | Context.MODE_MULTI_PROCESS | Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("name","xiaozhu") ;
        edit.putString("age","20") ;
        edit.commit();

```
### 跨进程将数据写入到SharedPreferences文件中

``` java

		try{
            // 通过包名得到另一个进程的上下文
            Context context = createPackageContext("com.zeno.ipc_fileshare",Context.CONTEXT_IGNORE_SECURITY) ;
            SharedPreferences sp = context.getSharedPreferences("sp",
                    Context.MODE_WORLD_WRITEABLE | Context.MODE_WORLD_READABLE | Context.MODE_MULTI_PROCESS);
            // 得到编辑对象
            SharedPreferences.Editor edit = sp.edit();
            // 装填数据
            edit.putString("name","xiaojiu") ;
            edit.putString("age","20") ;
            // 写入
            edit.commit();
        }catch(Exception e) {
            e.printStackTrace();
        }

````

##读取数据与上述操作类似
