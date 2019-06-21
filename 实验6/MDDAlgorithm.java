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
 * K-SHELL�Ż�
 * 
 * @author Bobby
 * @data 2019��3��18�� ����4:18:07
 */

public class MDDAlgorithm {

	public static List<Integer[]> txtList = null;// ��ȡtxt���ݻ�õ�LIst����
	public static HashMap<Integer, HashSet<Integer>> networks = null;// �罻�����HashMap����
	public static HashMap<Integer, Double> mixedDegree = null;// ��϶�
	public static HashMap<Integer, Integer> reminingDegree = null;// ʣ���
	public static HashMap<Integer, Integer> exhaustedDegree = null;// ɾ���ȣ����е�ɾ���ĵ㣩
	public static HashMap<Double, HashSet<Integer>> M_shellans = null;// ������
	public static final double Lambda = 0.7;// ����

	/**
	 * �������
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// ���ݶ�ȡ
		String dataPath = "C:\\Users\\Bobby\\Desktop\\email_1133_5451.txt";// �����ļ�·��
		readFile(dataPath);

		// �罻���繹��
		creatHashMap();

		System.out.println("�����ĳ���Ϊ" + networks.size());
		// �����Ƚڵ��MAP
		getexhaustedDegree();
		MDD();
		for(Double key :M_shellans.keySet()) {
			System.out.println(key + " " + M_shellans.get(key));
		}
//		System.out.println(M_shellans);

	}

	/**
	 * ��϶ȷֽ��㷨���������н�����Ҫ�̶�
	 */
	private static void MDD() {
		List<Integer> deletNode = new ArrayList<Integer>();
		Double minMixDegree =  null;
		do {
			getreminingDegree();// ����ʣ���
			getmixedDegree();// �����϶�
			minMixDegree = getminMixDegree();//��ȡ��ǰ��������С��϶�
//			System.out.println("��ǰ��С��϶ȣ�" + minMixDegree);
			for (Integer key : mixedDegree.keySet()) {
				if (mixedDegree.get(key) <= minMixDegree) {
					deletNode.add(key);
				}
			}
			for(Integer key  : deletNode) {
				saveMShell(key, minMixDegree);// ������
				// �����罻����
				// ɾ�����ѹ�ϵ
				for (Integer peo : networks.get(key)) {
					networks.get(peo).remove(key);
					exhaustedDegree.replace(peo, exhaustedDegree.get(peo) + 1);// �������ѵĶ���Ϣ
				}
				// ��������ɾ������
				networks.remove(key);
				// ��������˵Ļ�϶�
				mixedDegree.remove(key);
				// ��������˵�ʣ���
				reminingDegree.remove(key);
				// ��������˵�ɾ����
				exhaustedDegree.remove(key);
			}
			deletNode.clear();
		} while (mixedDegree.size() != 0);
	}

	/**
	 * ����M-Shell
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
	 * ��ȡ��ǰ��С��϶�ֵ
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
	 * ���㵱ǰ�����и������Ļ�϶�
	 */
	private static void getmixedDegree() {
		if (mixedDegree == null) {
			//��ʼ����Ļ�϶ȵ�����ʣ���
			mixedDegree = new HashMap<Integer, Double>();
			for (Integer key : reminingDegree.keySet()) {
				mixedDegree.put(key, (double) reminingDegree.get(key));
			}
			return;
		}
		//���ݻ�϶ȹ�ʽ����ʣ���
		for (Integer key : networks.keySet()) {
			mixedDegree.put(key, (Double) (reminingDegree.get(key) + Lambda * exhaustedDegree.get(key)));
		}
	}

	/**
	 * ���������еĸ�������Ѿ�ɾ���Ķ�
	 */
	private static void getexhaustedDegree() {
		if (exhaustedDegree == null)
			exhaustedDegree = new HashMap<Integer, Integer>();
		for (Integer key : networks.keySet()) {
			exhaustedDegree.put(key, 0);
		}
	}

	/**
	 * ���������еĸ�����㵱ǰ�Ķ�
	 */
	private static void getreminingDegree() {
		if (reminingDegree == null)
			reminingDegree = new HashMap<Integer, Integer>();
		for (Integer key : networks.keySet()) {
			reminingDegree.put(key, networks.get(key).size());
		}
	}

	/**
	 * �����罻������
	 */
	private static void creatHashMap() {
		if (networks == null)
			networks = new HashMap<Integer, HashSet<Integer>>();
		Iterator<Integer[]> it = txtList.iterator();
		Integer lineInteger[] = null;
		while (it.hasNext()) {
			lineInteger = it.next();
			/* System.out.println(lineInteger[0]+" "+lineInteger[1]); */
			// �����˵Ĺ�ϵ��ӵ�������
			addRealtionship(lineInteger[0], lineInteger[1]);
			addRealtionship(lineInteger[1], lineInteger[0]);
		}
	}

	/**
	 * ��networksΪ��Աpeo1�������peo2
	 * 
	 * @param peop1 ��Ա1id
	 * @param peop2 ��Ա2id
	 */
	private static void addRealtionship(Integer peop1, Integer peop2) {
		if (!networks.containsKey(peop1)) {
			networks.put(peop1, new HashSet());
		}
		networks.get(peop1).add(peop2);
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
