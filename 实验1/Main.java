package WangLong.Project1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Set;
import java.util.Vector;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;


/**
 * 读取txt数据，并利用HashMap和HashSet存储构建社交网络
 * @author Bobby
 * @data 2019年3月18日 下午4:18:07
 */

public class Main {
	public static List<Integer[]> txtList = null;//读取txt数据获得的LIst对象
	public static HashMap<Integer , HashSet<Integer>> networks =null;//社交网络的HashMap对象
	
	/**
	 * 	程序入口
	 * @param args
	 */
	public static void main(String[] args) {
		//数据读取
		String dataPath = "C:\\Users\\Bobby\\Desktop\\email_1133_5451.txt";//数据文件路径
		readFile(dataPath);
		//哈希表构建
		creatHashMap();
		//网络查询
		Scanner in = new Scanner(System.in); 
        Integer peo = null;
        String peos = null;
        Integer degree = null;
		while(true) {
			//找朋友
			System.out.print("输入查询的人员编号: ");
	        peo = in.nextInt();
			findRealation(peo);
			//判断关系：支持多人查询
			System.out.print("输入待判断关系的人员编号(以空格分隔，支持输入人数>=2): ");
			peos = in.nextLine();
			peos = in.nextLine();
			findFriend(peos);
			System.out.println();
		}
	}

	/**
	 * 在很多人中查询所有的朋友关系
	 * @param peos 查询的String对象
	 */
	private static void findFriend(String peos) {
		//确定待判断的人员个数
		Vector<Integer> vec = getPeoNumber(peos);
		//判断两人的关系
		System.out.println("--------- 查询到的结果：  - - - - - - - - -");
		int ansnum=0;
		for(Integer i=0;i<vec.size()-1;i++) {//n-1次遍历
			for(Integer j=i;j<vec.size();j++) {
				if(!networks.containsKey(vec.get(i))) {
					System.out.println(vec.get(i)+" \u001b[1;31m 此人不是该网络中的人! \u001b[0m");
					break;
				}
				if(judgeFridens(vec.get(i),vec.get(j))) {
					ansnum++;
					System.out.println(vec.get(i) + " 和 "+ vec.get(j) +" 是朋友!");
				}
			}
		}
		
		if(ansnum==0) System.out.println("这些人互相不认识！");
		else System.out.println("共有关系个数： " + ansnum + " 个");
		System.out.println("------------------------------------");
	}

	/**
	 * 判定两人是否为朋友关系
	 * @param i 人员id
	 * @param j 人员id
	 * @return 若两人是朋友返回true，否则返回false
	 */
	private static boolean judgeFridens(Integer i, Integer j) {
		if(networks.get(i).contains(j)) return true;
		return false;
	}



	/**
	 * 获得查询成员的Vector<Integer>集合
	 * @param peos 待判定关系的成员String对象
	 * @return  有效查询对象的Vector对象
	 */
	private static Vector<Integer> getPeoNumber(String peos) {
		/*System.out.println(peos);*/
		String peos_array[] = peos.split(" ");
		Vector<Integer> vec = new Vector<Integer>();
		for(String index : peos_array) {
			vec.add(Integer.parseInt(index));
		}
		//利用Set为Vector去重
		Set<Integer> set = new HashSet<Integer>(vec);
		vec=new Vector<Integer>(set);
		return vec;
	}

	/**
	 * 查询给定的人员的社交情况
	 * @param peo 待查询的人员id
	 */
	private static void findRealation(Integer peo) {
		System.out.println("---------"+peo + " 共有朋友数：" + networks.get(peo).size()+" - - - - - - - - -");
		for(Integer member : networks.get(peo)) {
			System.out.print(member+" ");
		}
		System.out.println();
		System.out.println("-----------------------------------");
	}

	/**
	 * 创建社交网网络
	 */
	private static void creatHashMap() {
		if(networks==null) networks = new HashMap<Integer , HashSet<Integer>>();
		Iterator<Integer[]> it = txtList.iterator();
		Integer lineInteger[] = null;
		while (it.hasNext()) {    
			lineInteger = it.next();    
            /*System.out.println(lineInteger[0]+" "+lineInteger[1]);*/ 
			//将两人的关系添加到网络中
			addRealtionship(lineInteger[0],lineInteger[1]);
			addRealtionship(lineInteger[1],lineInteger[0]);
           }  
	}

	/**
	 * 在networks为成员peo1添加朋友peo2
	 * @param peop1 人员1id
	 * @param peop2	人员2id
	 */
	private static void addRealtionship(Integer peop1, Integer peop2) {
		if(!networks.containsKey(peop1)) {
			networks.put(peop1, new HashSet());
		}
		networks.get(peop1).add(peop2);
	}

	/**
	 * 读入txt文件，并将数据文件中的数据保存在成员变量txtList中
	 * @param dataPath	数据文件路径
	 */
	public static void readFile(String dataPath) {
		if(txtList==null) txtList = new ArrayList<Integer[]>();
		File txt =new File(dataPath);
		Reader reader = null;
		BufferedReader buf = null;
		try {
			reader = new FileReader(txt);
			buf = new BufferedReader(reader);
			String str = null;
			while((str = buf.readLine())!=null){
				/*System.out.println(str);*/
				txtList.add(new Integer[]{Integer.parseInt(str.split(",")[0]) , Integer.parseInt(str.split(",")[1])});
			}
			buf.close();
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("文件不存在！");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("文件读取错误！");
			e.printStackTrace();
		}
	}

}
