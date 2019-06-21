package WangLong.Project6;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Main {

	public static List<Integer[]> txtList = null;// 读取txt数据获得的LIst对象
	public static HashMap<Integer, HashSet<Integer>> shopNetworks = null;// 用户-商品网络购物关系的HashMap对象
	public static Map<Integer, Double> goodMap = null;// 用户-商品网络购物关系的HashMap对象

	public static void main(String[] args) {
		// 读取数据文件
		String dataPath = "C:\\Users\\Bobby\\Desktop\\RYM_3378_4489_66408.txt";// 数据文件路径
		readFile(dataPath);
		// 哈希表构建
		creatHashMap();
		// 推荐算法
		recommendAlgorithm();
	}

	/**
	 * 推荐算法
	 */
	private static void recommendAlgorithm() {
		HashSet<Integer> userNo1 = null;
		//计算各个商品的相似度
		for (Entry<Integer, HashSet<Integer>> entry : shopNetworks.entrySet()) {
			if(entry.getKey()==1) {
				userNo1 = entry.getValue();//用户1的购买记录
				continue;
			}
			Double rate = getSimilarity(userNo1,entry.getValue());//获取相似度
			addRatetoGood(entry.getValue(),rate);//更新商品推荐度
		}
		//去除用户一购买的商品
		for(Integer goodID : userNo1) {
			goodMap.remove(goodID);
		}
		//排序
		sortGoodMap();
	}
	/**
	 * 排序
	 */
	private static void sortGoodMap() {
		// 升序比较器
		Comparator<Map.Entry<Integer, Double>> valueComparator = new Comparator<Map.Entry<Integer,Double>>() {
			@Override
			public int compare(Entry<Integer, Double> o1,Entry<Integer, Double> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
		};
		// map转换成list进行排序
		List<Map.Entry<Integer, Double>> list = new ArrayList<Map.Entry<Integer,Double>>(goodMap.entrySet());
		// 排序
		Collections.sort(list,valueComparator);
		// 默认情况下，TreeMap对key进行升序排序
//		for (Entry<Integer, Double> entry : list) {
//		System.out.println(entry.getKey() + ":" + entry.getValue());
//		}
		for(int i=list.size()-1;i>list.size()-11;i--) {
			System.out.println(list.get(i).getKey() + ":" + list.get(i).getValue());
		}
	}

	/**
	 * 每个用户计算得到rate后更新goodMap
	 * @param goods
	 * @param rate
	 */
	private static void addRatetoGood(HashSet<Integer> goods, Double rate) {
		for(Integer goodID : goods ) {
			goodMap.put(goodID, goodMap.get(goodID)+rate);
//			System.out.println(goodMap.get(goodID).doubleValue());
		}
	}

	/**
	 * 计算两个用户的相似度
	 * @param userNo1 用户1的购买记录
	 * @param userNon 用户n的购买记录
	 * @return 相似度
	 */
	private static Double getSimilarity(HashSet<Integer> userNo1, HashSet<Integer> userNon) {
		HashSet<Integer> union = new HashSet<Integer>();
		union.addAll(userNo1);
		union.addAll(userNon);
		int unionSize = union.size();//两位用户购买商品总数（重复的算一次）
		int intersectionSize = userNon.size()-unionSize+userNo1.size();//两位用户共同购买商品数
		return (double)intersectionSize/unionSize ;//相似度
	}

	/**
	 * 创建用户-商品网络;以及商品列表
	 */
	private static void creatHashMap() {
		if (shopNetworks == null)
			shopNetworks = new HashMap<Integer, HashSet<Integer>>();
		Iterator<Integer[]> it = txtList.iterator();
		Integer lineInteger[] = null;
		while (it.hasNext()) {
			lineInteger = it.next();
			addRealtionship(lineInteger[0], lineInteger[1]);//添加用户购买记录
			addGoodstoMap(lineInteger[1]);//在商品表中添加商品信息
		}
	}

	/**
	 * 将所有出现的商品保存在goodMap中
	 * 
	 * @param goodsID
	 */
	private static void addGoodstoMap(Integer goodsID) {
		if (goodMap == null) {
			goodMap = new TreeMap<Integer, Double>();
		}
		if (goodMap.containsKey(goodsID))
			return;
		else
			goodMap.put(goodsID, 0.0);
	}

	/**
	 * 在shopNetworks为每个用户添加购物历史
	 * 
	 * @param peopID  人员1id
	 * @param goodsID 商品id
	 */
	private static void addRealtionship(Integer peopID, Integer goodsID) {
		if (!shopNetworks.containsKey(peopID)) {
			shopNetworks.put(peopID, new HashSet());
		}
		shopNetworks.get(peopID).add(goodsID);
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
