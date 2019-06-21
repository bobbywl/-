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

	public static List<Integer[]> txtList = null;// ��ȡtxt���ݻ�õ�LIst����
	public static HashMap<Integer, HashSet<Integer>> shopNetworks = null;// �û�-��Ʒ���繺���ϵ��HashMap����
	public static Map<Integer, Double> goodMap = null;// �û�-��Ʒ���繺���ϵ��HashMap����

	public static void main(String[] args) {
		// ��ȡ�����ļ�
		String dataPath = "C:\\Users\\Bobby\\Desktop\\RYM_3378_4489_66408.txt";// �����ļ�·��
		readFile(dataPath);
		// ��ϣ����
		creatHashMap();
		// �Ƽ��㷨
		recommendAlgorithm();
	}

	/**
	 * �Ƽ��㷨
	 */
	private static void recommendAlgorithm() {
		HashSet<Integer> userNo1 = null;
		//���������Ʒ�����ƶ�
		for (Entry<Integer, HashSet<Integer>> entry : shopNetworks.entrySet()) {
			if(entry.getKey()==1) {
				userNo1 = entry.getValue();//�û�1�Ĺ����¼
				continue;
			}
			Double rate = getSimilarity(userNo1,entry.getValue());//��ȡ���ƶ�
			addRatetoGood(entry.getValue(),rate);//������Ʒ�Ƽ���
		}
		//ȥ���û�һ�������Ʒ
		for(Integer goodID : userNo1) {
			goodMap.remove(goodID);
		}
		//����
		sortGoodMap();
	}
	/**
	 * ����
	 */
	private static void sortGoodMap() {
		// ����Ƚ���
		Comparator<Map.Entry<Integer, Double>> valueComparator = new Comparator<Map.Entry<Integer,Double>>() {
			@Override
			public int compare(Entry<Integer, Double> o1,Entry<Integer, Double> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
		};
		// mapת����list��������
		List<Map.Entry<Integer, Double>> list = new ArrayList<Map.Entry<Integer,Double>>(goodMap.entrySet());
		// ����
		Collections.sort(list,valueComparator);
		// Ĭ������£�TreeMap��key������������
//		for (Entry<Integer, Double> entry : list) {
//		System.out.println(entry.getKey() + ":" + entry.getValue());
//		}
		for(int i=list.size()-1;i>list.size()-11;i--) {
			System.out.println(list.get(i).getKey() + ":" + list.get(i).getValue());
		}
	}

	/**
	 * ÿ���û�����õ�rate�����goodMap
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
	 * ���������û������ƶ�
	 * @param userNo1 �û�1�Ĺ����¼
	 * @param userNon �û�n�Ĺ����¼
	 * @return ���ƶ�
	 */
	private static Double getSimilarity(HashSet<Integer> userNo1, HashSet<Integer> userNon) {
		HashSet<Integer> union = new HashSet<Integer>();
		union.addAll(userNo1);
		union.addAll(userNon);
		int unionSize = union.size();//��λ�û�������Ʒ�������ظ�����һ�Σ�
		int intersectionSize = userNon.size()-unionSize+userNo1.size();//��λ�û���ͬ������Ʒ��
		return (double)intersectionSize/unionSize ;//���ƶ�
	}

	/**
	 * �����û�-��Ʒ����;�Լ���Ʒ�б�
	 */
	private static void creatHashMap() {
		if (shopNetworks == null)
			shopNetworks = new HashMap<Integer, HashSet<Integer>>();
		Iterator<Integer[]> it = txtList.iterator();
		Integer lineInteger[] = null;
		while (it.hasNext()) {
			lineInteger = it.next();
			addRealtionship(lineInteger[0], lineInteger[1]);//����û������¼
			addGoodstoMap(lineInteger[1]);//����Ʒ���������Ʒ��Ϣ
		}
	}

	/**
	 * �����г��ֵ���Ʒ������goodMap��
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
	 * ��shopNetworksΪÿ���û���ӹ�����ʷ
	 * 
	 * @param peopID  ��Ա1id
	 * @param goodsID ��Ʒid
	 */
	private static void addRealtionship(Integer peopID, Integer goodsID) {
		if (!shopNetworks.containsKey(peopID)) {
			shopNetworks.put(peopID, new HashSet());
		}
		shopNetworks.get(peopID).add(goodsID);
	}

	/**
	 * ����txt�ļ������������ļ��е����ݱ����ڳ�Ա����txtList��
	 * 
	 * @param dataPath �����ļ�·��
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
			System.out.println("�ļ������ڣ�");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("�ļ���ȡ����");
			e.printStackTrace();
		}
	}

}
