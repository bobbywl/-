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
 * ��ȡtxt���ݣ�������HashMap��HashSet�洢�����罻����
 * @author Bobby
 * @data 2019��3��18�� ����4:18:07
 */

public class Main {
	public static List<Integer[]> txtList = null;//��ȡtxt���ݻ�õ�LIst����
	public static HashMap<Integer , HashSet<Integer>> networks =null;//�罻�����HashMap����
	
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
		//�����ѯ
		Scanner in = new Scanner(System.in); 
        Integer peo = null;
        String peos = null;
        Integer degree = null;
		while(true) {
			//������
			System.out.print("�����ѯ����Ա���: ");
	        peo = in.nextInt();
			findRealation(peo);
			//�жϹ�ϵ��֧�ֶ��˲�ѯ
			System.out.print("������жϹ�ϵ����Ա���(�Կո�ָ���֧����������>=2): ");
			peos = in.nextLine();
			peos = in.nextLine();
			findFriend(peos);
			System.out.println();
		}
	}

	/**
	 * �ںܶ����в�ѯ���е����ѹ�ϵ
	 * @param peos ��ѯ��String����
	 */
	private static void findFriend(String peos) {
		//ȷ�����жϵ���Ա����
		Vector<Integer> vec = getPeoNumber(peos);
		//�ж����˵Ĺ�ϵ
		System.out.println("--------- ��ѯ���Ľ����  - - - - - - - - -");
		int ansnum=0;
		for(Integer i=0;i<vec.size()-1;i++) {//n-1�α���
			for(Integer j=i;j<vec.size();j++) {
				if(!networks.containsKey(vec.get(i))) {
					System.out.println(vec.get(i)+" \u001b[1;31m ���˲��Ǹ������е���! \u001b[0m");
					break;
				}
				if(judgeFridens(vec.get(i),vec.get(j))) {
					ansnum++;
					System.out.println(vec.get(i) + " �� "+ vec.get(j) +" ������!");
				}
			}
		}
		
		if(ansnum==0) System.out.println("��Щ�˻��಻��ʶ��");
		else System.out.println("���й�ϵ������ " + ansnum + " ��");
		System.out.println("------------------------------------");
	}

	/**
	 * �ж������Ƿ�Ϊ���ѹ�ϵ
	 * @param i ��Աid
	 * @param j ��Աid
	 * @return �����������ѷ���true�����򷵻�false
	 */
	private static boolean judgeFridens(Integer i, Integer j) {
		if(networks.get(i).contains(j)) return true;
		return false;
	}



	/**
	 * ��ò�ѯ��Ա��Vector<Integer>����
	 * @param peos ���ж���ϵ�ĳ�ԱString����
	 * @return  ��Ч��ѯ�����Vector����
	 */
	private static Vector<Integer> getPeoNumber(String peos) {
		/*System.out.println(peos);*/
		String peos_array[] = peos.split(" ");
		Vector<Integer> vec = new Vector<Integer>();
		for(String index : peos_array) {
			vec.add(Integer.parseInt(index));
		}
		//����SetΪVectorȥ��
		Set<Integer> set = new HashSet<Integer>(vec);
		vec=new Vector<Integer>(set);
		return vec;
	}

	/**
	 * ��ѯ��������Ա���罻���
	 * @param peo ����ѯ����Աid
	 */
	private static void findRealation(Integer peo) {
		System.out.println("---------"+peo + " ������������" + networks.get(peo).size()+" - - - - - - - - -");
		for(Integer member : networks.get(peo)) {
			System.out.print(member+" ");
		}
		System.out.println();
		System.out.println("-----------------------------------");
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
