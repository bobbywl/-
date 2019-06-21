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
 * ��ȡtxt���ݣ�������HashMap��HashSet�洢�����罻����
 * @author Bobby
 * @data 2019��3��18�� ����4:18:07
 */

public class KShell {
	
	public static List<Integer[]> txtList = null;//��ȡtxt���ݻ�õ�LIst����
	public static HashMap<Integer , HashSet<Integer>> networks =null;//�罻�����HashMap����
	public static HashMap<Integer , Integer> degreemap =null;//�罻�����HashMap����
	public static HashMap<Integer , Integer> K_shellans =null;//�罻�����HashMap����
	/**
	 * 	�������
	 * @param args
	 */
	public static void main(String[] args) {
		//���ݶ�ȡ
		String dataPath = "C:\\Users\\Bobby\\Desktop\\email_1133_5451.txt";//�����ļ�·��
		readFile(dataPath);
		//��ϣ����
		creatHashMap();
		System.out.println("�����ĳ���Ϊ"+networks.size());
		//�����Ƚڵ��MAP
//		creatDereeMap();
		//��ѯMAP�ж���С�ļ���,���罻������ɾ����ͬʱզK_shellans�б����
		k_shell();
		System.out.println(K_shellans);
	
	}


	/**
	 * 	�ҵ���С�Ķȳ���
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
			System.out.println("����С�Ķ�Ϊ��"+minnodedegree);
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
	 * �����ڵ�Ķ���ϢMap
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
	 * �����罻������
	 */
	private static void creatHashMap() {
		if(networks==null) networks = new HashMap<Integer , HashSet<Integer>>();
		Iterator<Integer[]> it = txtList.iterator();
		Integer lineInteger[] = null;
		while (it.hasNext()) {    
			lineInteger = it.next();    
            /*System.out.println(lineInteger[0]+" "+lineInteger[1]);*/ 
			//�����˵Ĺ�ϵ��ӵ�������
			addRealtionship(lineInteger[0],lineInteger[1]);
			addRealtionship(lineInteger[1],lineInteger[0]);
           }  
	}

	/**
	 * ��networksΪ��Աpeo1�������peo2
	 * @param peop1 ��Ա1id
	 * @param peop2	��Ա2id
	 */
	private static void addRealtionship(Integer peop1, Integer peop2) {
		if(!networks.containsKey(peop1)) {
			networks.put(peop1, new HashSet());
		}
		networks.get(peop1).add(peop2);
	}

	/**
	 * ����txt�ļ������������ļ��е����ݱ����ڳ�Ա����txtList��
	 * @param dataPath	�����ļ�·��
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
			System.out.println("�ļ������ڣ�");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("�ļ���ȡ����");
			e.printStackTrace();
		}
	}

}
