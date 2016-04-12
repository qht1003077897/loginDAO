看这篇文章之前请大家先去简单看一下这3个东西都是干嘛用的，其实这篇文章的主旨还是讲解我自己对MVP的理解。
**我在这里分三段讲解，先从GreenDao开始：**

 ---------------------说白了greendao就是安卓操作sqlite的一个第三方框架，通过在java项目中写一段代码然后生成一个android项目，此生成的安卓项目中包含操作数据库的类。不同于其他框架比如OrmLite和LitePal等其他一些优秀的框架，greenDao主要是需要一个java项目去生成一系列安卓实体类，但是在使用起来却是方便过其他框架的。---------------------------------

 

 - 第一步：

首先新建一个java项目，并在libs下面添加如图的三个包，给出下载地址：
http://download.csdn.net/detail/u012534831/9487105
![这里写图片描述](http://img.blog.csdn.net/20160411112455120)

然后新建java文件，并写如下所示的一段java代码

```
/**
 - Created by QHT on 16/04/01.
 */
public class ExampleDaoGenerator
{      
    public static void main(String[] args) throws Exception  
    {   
        // 创建Schema对象
        // 第一个参数为数据库版本号
        // 第二个参数为自动生成的实体类将要存放在安卓项目中的位置,com.example为我的包名（默认） 
    Schema schema = new Schema(1, "com.example.model" );  
    addUser(schema);
    //loginDAO为我的安卓项目名
    new DaoGenerator().generateAll(schema, "../loginDAO/src" );  
    }  
private static void addUser(Schema schema)   
    { 
    //添加的实体名称，最后生成的DAO和Session都是"USER"开头
        Entity note = schema.addEntity( "USER");      
        note.addIdProperty();    
        note.addStringProperty( "username").notNull();   
        note.addStringProperty( "password").notNull();  
        }
}
```
 - 第二步：

运行此java程序，会在Console打印下图的内容：
![](http://img.blog.csdn.net/20160411144337850)
可以看出，生成了4个实体类，除过USER.java一个javabean，其他都是数据库有关的类，是唯一暴露到上层的部分框架代码。 这时候refresh我们的安卓项目，可以看到下图内容：
![这里写图片描述](http://img.blog.csdn.net/20160411144905376)

 - 第三步：现在只需要下面这些代码就可以操作数据库了


```
 USERDao userdao = SingleType.getInstance().getuserDao();
		SQLiteOpenHelper helper = new DaoMaster.DevOpenHelper(context,"user.db",null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        userDao = daoSession.getUSERDao();
        //上面的全部是初始化过程
	    UserBean user = new UserBean((long)2, UserBean.getUsername(), UserBean.getPassword());
	    daoslist.add(user);   
	    userDao.insertInTx(daoslist);
```
至此，第一大步结束，第二步关于OKhttp的使用看下面这个博客：

> （http://blog.csdn.net/lmj623565791/article/details/47911083）


----------
**第三大步，MVP的使用**。
MVP是MVC的升级版，在MVP中，我们的业务逻辑也即model是要完全和我们的数据显示即为view脱离关系，M:model. V:view.  P:presenter.从表面来看，presenter就像一个管理者，既管理着View，还管理者model。但是，view和model还要一个不认识一个。在我看来，这就是强硬解耦，有好处，有坏处。

 - 好处1：
使Activity的代码变的干净整洁，尤其是项目变的庞大的时候，我们的activity经常很臃肿，有时候修改个东西，要不半天找不到，要不牵扯太多。
 - 好处2：
所有的耗时操作，复杂的业务逻辑都放到model中进行处理，model只提供向上的供presenter操作的方法。view层展示我们的界面。这样当我们要修改一些代码时，不用去动activity，直接去model中找对应的方法即可。
 - 好处3：对activity对象通过实现接口注入式调用，有效避免内存泄露。
 
 - 坏处1：代码冗余度非常大，一会可以看到一个小例子的代码量，并且还有成倍增加的接口和类
 
 
首先，看一下项目结构
![这里写图片描述](http://img.blog.csdn.net/20160411153206627)

```
/**
 * @author QHT
 */
public interface IUserModel {
//判断用户名和密码的合法性
	void judgmentNameANDPass(String username,String password);
//okhttp请求
	void httprequest(String username,String password);
//greenDAO数据库保存
	void doGreenDAO(Context context);
}
```
这是model的接口，有三个方法，供model实现类去实现。

```
public class UserModelIml implements IUserModel {
	    private SQLiteDatabase db;
		private DaoMaster daoMaster;
		private DaoSession daoSession;
		private USERDao userDao; 
		private Ipresenter presenter;
		private List<UserBean> daoslist;
		public int ifsuccess=0;
		//构造方法中通过注入式的方式去实现presenter的方法，这样避免了model对presenter的持有（以下犯上）
	public UserModelIml(Ipresenter view){
		this.daoslist=new ArrayList<UserBean>();
		presenter=view;
	}
	public void  judgmentNameANDPass(String username, String password) {
		// TODO Auto-generated method stub
	if(username==null||username.isEmpty()||password==null||password.isEmpty()){   
      }else{
    	  Util.judgeNullMsg=1;
    	  UserBean.setUsername(username);
    	  UserBean.setPassword(password);
      }
		return ;
	}
	@Override
	public void  httprequest(String username, String password) {
		// TODO Auto-generated method stub
		  OkHttpUtils
	      .get()//
	      .url(Util.getUrl())
	      .addParams("username", username)//
	      .addParams("password", password)//
	      .build()//
	      .execute(new UserCallback()
	      {
	          @Override
	          public void onError(Call call, Exception e)
	          {
	          }
	          public void onResponse(UserBean response)
	          {
	          //请求成功的结果，我服务端写的是如果成功，返回"login success"
	        	String responsestring=response.getmsg();
	        	
	          	if(responsestring.equals("login success")){
	          		Util.requestMsg=1;
	          		presenter.requestSuccess();
	          }else{
	        	  presenter.requestError();
	          }
	         }
	      });
		  return;
	}
	@Override
	//下面这段代码其实读者可以自己去写一个单例，然后封装初始化操作，通过下面的方式调用，我这儿没写，主要是之前写了但是调试又出了点问题，到现在还没解决，哈哈
	//USERDao userdao = SingleType.getInstance().getuserDao();
	public void doGreenDAO(Context context) {
		SQLiteOpenHelper helper = new DaoMaster.DevOpenHelper(context,"user.db",null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        userDao = daoSession.getUSERDao();
      //ID,用户名，密码
	    UserBean user = new UserBean((long)2, UserBean.getUsername(), UserBean.getPassword());
	    daoslist.add(user);   
	    //这儿支持直接将list放入表中
	    userDao.insertInTx(daoslist);
	    //这儿这个标志下面会用到，保存成功置1	
	    Util.doGreenDaoMsg=1;
	}
}
```
这是model的实现类UserModelIml ，里面就是网络操作和数据库操作。

```
/**
 * @author QHT
 *
 */
public interface IUserView{
	//界面信息展示
	void showInfo();
	//操作数据库
	void dogreendao();
	//跳转
	void toTwoActivity();
	//用户名密码错误
	void ToastjudgeError();
	//登录错误
	void ToastrequestError();
	//登录成功
	void ToastrequestSuccess();
}
```
这是View借口，供avtivity实现，并通过注入式的方式调用里面的方法。

```
public interface Ipresenter {
	//用户名和密码判断
  void judgeerror();
  //登录错误和成功
  void requestError();
  void requestSuccess();
}
```
这是presenter接口，供presenter类实现。

```
* @author QHT
 *
 */
 public class UserPresenter implements Ipresenter{
	 //同时持有Model和View的引用
	UserModelIml userModelIml;
	UserBean userBean;
	MainActivity mainActivity;
	IUserView iUserView;
public UserPresenter(IUserView view){
	this.userModelIml=new UserModelIml(this);
	this.userBean =new UserBean();
	iUserView=view;
}
public void judge(String userName, String password){
	  userModelIml.judgmentNameANDPass(userName, password);
	  //这儿如果model中的判断用户名返回1，才去执行网络登录操作
	  if(Util.judgeNullMsg==1){
		  userModelIml.httprequest(userName, password);
	  }else{
		judgeerror();  
	  }
	  return;
}
//下面其实都是activity的方法
public void dodatebase(Context context){
	// TODO Auto-generated method stub
	userModelIml.doGreenDAO(context);
}
public void toactivityANDshow(int i){
	iUserView.toTwoActivity();
 }
public void judgeerror(){
	iUserView.ToastjudgeError();	 
}
public void requestError(){
	 if(Util.requestMsg==0)
	   {
		 iUserView.ToastrequestError();
	   }
}
public void requestSuccess(){
	     iUserView.dogreendao();
		 iUserView.ToastrequestSuccess();
	 toactivityANDshow(Util.requestMsg);
 }
}
```
UserPresenter 类，很明显是model和view的交互，相当于中间件。

```
public class MainActivity extends ActionBarActivity implements IUserView{
	private EditText	user;
	private TextView    login;
	private EditText	pass;
	private TextView ttt;
	private String tt,tttt;
	private UserPresenter userPresenter;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
	private void initView() {
		// TODO Auto-generated method stub
			login=(TextView)findViewById(R.id.login);
			ttt=(TextView)findViewById(R.id.tt);
			user=(EditText)findViewById(R.id.username);
			pass=(EditText)findViewById(R.id.password);
			userPresenter=new UserPresenter(this);
	}
	//登录操作
		public void login(View view) 
	{
			// TODO Auto-generated method stub
			 userPresenter.judge(getuserName(),getpassword());
	}
	public String getuserName() {
		// TODO Auto-generated method stub
		return user.getText().toString();
	}
	public String getpassword() {
		// TODO Auto-generated method stub
		return pass.getText().toString();
	}
	public void toTwoActivity() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(MainActivity.this,TWOActivity.class);
		startActivity(intent);
	}
	@Override
	public void ToastjudgeError() {
		// TODO Auto-generated method stub
		Toast.makeText(MainActivity.this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
	}
	@Override
	public void ToastrequestError() {
		// TODO Auto-generated method stub
		Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
	}
	@Override
	public void ToastrequestSuccess() {
		// TODO Auto-generated method stub
		showInfo();
		Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
	}
	@Override
	public void showInfo() {
		// TODO Auto-generated method stub
		ttt.setText(UserBean.getUsername());
	}
	public void dogreendao(){
		userPresenter.dodatebase(this);
	}
 }
```
看，activity很简洁吧，就是几行代码，并且只和并且生命周期或者一些对象的持有并不会造成多大的问题，当然，还有个工具类在下面

```
/**
 * @author QHT
 *
 */
package com.example.model;
public class Util{  
   
    public class Constants {
  	   public static final String DB_NAME = "person.db";  
  }
     public static String getUrl(){
   	   final String URL = 
   	   //这是我的服务端地址，下面可以给出我的服务端几行代码
   	   "http://10.50.63.129/httptest/getrequest";
   	   return URL;
   } 
     public static  int judgeNullMsg=0;
     public static  int requestMsg=0;
     public static  int doGreenDaoMsg=0;
}  
```

```
public class QhtServlet extends HttpServlet {
 public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        //获取页面传入的参数
        String name  = request.getParameter("username");
        String password  = request.getParameter("password");
        PrintWriter printWriter = response.getWriter();
        JSONObject jsonObject = new JSONObject();
        if(!name.equals("111")){
             jsonObject.put("msg", "login filed");
             printWriter.write(jsonObject.toString());
    }else if(name.equals("111")){
     jsonObject.put("msg", "login success");
             printWriter.write(jsonObject.toString());
    }
    }
```
附上源码地址：https://github.com/qht1003077897/loginDAO.git

