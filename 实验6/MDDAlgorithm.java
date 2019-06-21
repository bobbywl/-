package WangLong.Project5;

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
 * K-SHELL优化
 * 
 * @author Bobby
 * @data 2019年3月18日 下午4:18:07
 */

public class MDDAlgorithm {

	public static List<Integer[]> txtList = null;// 读取txt数据获得的LIst对象
	public static HashMap<Integer, HashSet<Integer>> networks = null;// 社交网络的HashMap对象
	public static HashMap<Integer, Double> mixedDegree = null;// 混合度
	public static HashMap<Integer, Integer> reminingDegree = null;// 剩余度
	public static HashMap<Integer, Integer> exhaustedDegree = null;// 删除度（所有的删除的点）
	public static HashMap<Double, HashSet<Integer>> M_shellans = null;// 保存结果
	public static final double Lambda = 0.7;// 比例

	/**
	 * 程序入口
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// 数据读取
		String dataPath = "C:\\Users\\Bobby\\Desktop\\email_1133_5451.txt";// 数据文件路径
		readFile(dataPath);

		// 社交网络构建
		creatHashMap();

		System.out.println("构建的长度为" + networks.size());
		// 创建度节点的MAP
		getexhaustedDegree();
		MDD();
		for(Double key :M_shellans.keySet()) {
			System.out.println(key + " " + M_shellans.get(key));
		}
//		System.out.println(M_shellans);

	}

	/**
	 * 混合度分解算法计算网络中结点的重要程度
	 */
	private static void MDD() {
		List<Integer> deletNode = new ArrayList<Integer>();
		Double minMixDegree =  null;
		do {
			getreminingDegree();// 计算剩余度
			getmixedDegree();// 计算混合度
			minMixDegree = getminMixDegree();//获取当前网络中最小混合度
//			System.out.println("当前最小混合度：" + minMixDegree);
			for (Integer key : mixedDegree.keySet()) {
				if (mixedDegree.get(key) <= minMixDegree) {
					deletNode.add(key);
				}
			}
			for(Integer key  : deletNode) {
				saveMShell(key, minMixDegree);// 保存结果
				// 更新社交网络
				// 删除朋友关系
				for (Integer peo : networks.get(key)) {
					networks.get(peo).remove(key);
					exhaustedDegree.replace(peo, exhaustedDegree.get(peo) + 1);// 更新朋友的度信息
				}
				// 在网络中删除该人
				networks.remove(key);
				// 不计算该人的混合度
				mixedDegree.remove(key);
				// 不计算该人的剩余度
				reminingDegree.remove(key);
				// 不计算该人的删除度
				exhaustedDegree.remove(key);
			}
			deletNode.clear();
		} while (mixedDegree.size() != 0);
	}

	/**
	 * 保存M-Shell
	 * 
	 * @param key
	 * @param minMixDegree
	 */
	private static void saveMShell(Integer key, double minMixDegree) {
		if (M_shellans == null)
			M_shellans = new HashMap<Double, HashSet<Integer>>();
		if (!M_shellans.containsKey(minMixDegree))
			M_shellans.put(minMixDegree, new HashSet<Integer>());
		M_shellans.get(minMixDegree).add(key);
	}

	/**
	 * 获取当前最小混合度值
	 * 
	 * @return
	 */
	private static double getminMixDegree() {
		Double mixed = null;
		for (Integer node : mixedDegree.keySet()) {
			if (mixed == null) {
				mixed = mixedDegree.get(node);
				continue;
			}
			if (mixedDegree.get(node) < mixed)
				mixed = mixedDegree.get(node);
		}
		return mixed.doubleValue();
	}

	/**
	 * 计算当前网络中各个结点的混合度
	 */
	private static void getmixedDegree() {
		if (mixedDegree == null) {
			//初始网络的混合度等于其剩余度
			mixedDegree = new HashMap<Integer, Double>();
			for (Integer key : reminingDegree.keySet()) {
				mixedDegree.put(key, (double) reminingDegree.get(key));
			}
			return;
		}
		//依据混合度公式计算剩余度
		for (Integer key : networks.keySet()) {
			mixedDegree.put(key, (Double) (reminingDegree.get(key) + Lambda * exhaustedDegree.get(key)));
		}
	}

	/**
	 * 计算网络中的各个结点已经删除的度
	 */
	private static void getexhaustedDegree() {
		if (exhaustedDegree == null)
			exhaustedDegree = new HashMap<Integer, Integer>();
		for (Integer key : networks.keySet()) {
			exhaustedDegree.put(key, 0);
		}
	}

	/**
	 * 计算网络中的各个结点当前的度
	 */
	private static void getreminingDegree() {
		if (reminingDegree == null)
			reminingDegree = new HashMap<Integer, Integer>();
		for (Integer key : networks.keySet()) {
			reminingDegree.put(key, networks.get(key).size());
		}
	}

	/**
	 * 创建社交网网络
	 */
	private static void creatHashMap() {
		if (networks == null)
			networks = new HashMap<Integer, HashSet<Integer>>();
		Iterator<Integer[]> it = txtList.iterator();
		Integer lineInteger[] = null;
		while (it.hasNext()) {
			lineInteger = it.next();
			/* System.out.println(lineInteger[0]+" "+lineInteger[1]); */
			// 将两人的关系添加到网络中
			addRealtionship(lineInteger[0], lineInteger[1]);
			addRealtionship(lineInteger[1], lineInteger[0]);
		}
	}

	/**
	 * 在networks为成员peo1添加朋友peo2
	 * 
	 * @param peop1 人员1id
	 * @param peop2 人员2id
	 */
	private static void addRealtionship(Integer peop1, Integer peop2) {
		if (!networks.containsKey(peop1)) {
			networks.put(peop1, new HashSet());
		}
		networks.get(peop1).add(peop2);
	}

	/**
	 * 读入txt文件，并将数据文件中的数据保存在成员变量txtList中
	 * 
	 * @param dataPath 数据文件路径
	 */
	public static void readFile(String dataPath) {
		if (txtList == null)
			txtList = new ArrayList<Integer[]>();
		File txt = new File(dataPath);
		Reader reader = null;
		BufferedReader buf = null;
		try {
			reader = new FileReader(txt);
			buf = new BufferedReader(reader);
			String str = null;
			while ((str = buf.readLine()) != null) {
				/* System.out.println(str); */
				txtList.add(new Integer[] { Integer.parseInt(str.split(",")[0]), Integer.parseInt(str.split(",")[1]) });
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
