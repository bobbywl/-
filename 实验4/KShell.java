package WangLong.Project4;

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

public class KShell {
	
	public static List<Integer[]> txtList = null;//读取txt数据获得的LIst对象
	public static HashMap<Integer , HashSet<Integer>> networks =null;//社交网络的HashMap对象
	public static HashMap<Integer , Integer> degreemap =null;//社交网络的HashMap对象
	public static HashMap<Integer , Integer> K_shellans =null;//社交网络的HashMap对象
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
		System.out.println("构建的长度为"+networks.size());
		//创建度节点的MAP
//		creatDereeMap();
		//查询MAP中度最小的集合,从社交网络中删除，同时咋K_shellans中保结果
		k_shell();
		System.out.println(K_shellans);
	
	}


	/**
	 * 	找到最小的度长度
	 * @return
	 */
	private static Integer findMinDegree() {
		Integer nodeindex = null;
		for(Integer node : degreemap.keySet()) {
			if(degreemap.get(node)==null) {continue;}
			if(nodeindex==null) {nodeindex = degreemap.get(node);continue;}
			if(degreemap.get(node)<nodeindex) {nodeindex=degreemap.get(node);}
		}
		return nodeindex;
	}

	/**
	 * 	k-shell
	 */
	private static void k_shell() {
		creatDereeMap();
		if(K_shellans == null) {K_shellans =new HashMap<Integer , Integer>();}
		Integer minnodedegree = findMinDegree();
		while(minnodedegree!=null) {
			System.out.println("当最小的度为："+minnodedegree);
			for(Integer key : degreemap.keySet()) {
				if(degreemap.get(key)==minnodedegree) {
					K_shellans.put(key, minnodedegree);
					degreemap.put(key, null);
					for(Integer peo : networks.get(key)) {
						if(degreemap.get(peo)!=null) {degreemap.put(peo, degreemap.get(peo)-1);}
					}
				}
			}
			minnodedegree = findMinDegree();
		}
	}


	/**
	 * 创建节点的度信息Map
	 */
	private static void creatDereeMap() {
		if (degreemap == null) {
			degreemap = new HashMap<Integer, Integer>();
		}
		for (Integer key : networks.keySet()) {
			degreemap.put(key, networks.get(key).size());
		}
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
