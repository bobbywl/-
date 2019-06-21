package WangLong.Project3;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Set;
import java.util.Vector;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * ��ȡtxt���ݣ�������HashMap��HashSet�洢�����罻���� ����HashMap��Vector�洢
 * 
 * @author Bobby
 * @data 2019��4��8�� ����4:18:07
 */

public class Main {
	public static List<Integer[]> txtList = null;// ��ȡtxt���ݻ�õ�LIst����
	public static HashMap<Integer, HashSet<Integer>> networks = null;// �罻�����HashMap����
	public static HashMap<Integer, Vector<Integer>> DegreeMap = null;// �����нڵ����Ϣ��HashMap����

	/**
	 * �������
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// ���ݶ�ȡ
		String dataPath = "C:\\Users\\Bobby\\Desktop\\email_1133_5451.txt";// �����ļ�·��
		readFile(dataPath);
		// ��ϣ����
		creatHashMap();
		// �ڵ�ȵ�ͳ��
		creatDegreeMap();
		// �����ѯ
		Scanner in = new Scanner(System.in);
		Integer degree = null;
		// ʹ������ͼ��ʾ
		creatLineChart(DegreeMap);
		while (true) { 
			// ���Ҷ�
			System.out.print("�����ѯ�Ķ���: ");
			try {
				degree = in.nextInt();
				getDegree(degree);
			}catch(Exception e) {
				System.out.println("û�нڵ�Ķ�Ϊ��"+ degree+ "");
			}
			System.out.println();
		}
	}

	private static void creatLineChart(HashMap<Integer, Vector<Integer>> data) {
		// ͼ����������
		StandardChartTheme mChartTheme = new StandardChartTheme("CN");
		mChartTheme.setLargeFont(new Font("����", Font.BOLD, 15));
		mChartTheme.setExtraLargeFont(new Font("����", Font.PLAIN, 15));
		mChartTheme.setRegularFont(new Font("����", Font.PLAIN, 15));
		ChartFactory.setChartTheme(mChartTheme);

		// ͼ�����ݼ�����
		CategoryDataset mDataset = GetDataset(data);

		// ͼ���� ��ʾ��������
		JFreeChart mChart = ChartFactory.createLineChart("", // ͼ����
				"�ڵ�Ķ�", // ������
				"����", // ������
				mDataset, // ���ݼ�
				PlotOrientation.VERTICAL, false, // ��ʾͼ��
				true, // ���ñ�׼������
				false);// �Ƿ����ɳ�����
		// ������������
		CategoryPlot mPlot = (CategoryPlot) mChart.getPlot();
		mPlot.setBackgroundPaint(Color.LIGHT_GRAY);// ���õײ�����ɫ
		mPlot.setRangeGridlinePaint(Color.WHITE); // ����ˮƽ���򱳾�����ɫ
		mPlot.setRangeGridlinesVisible(true);// �����Ƿ���ʾˮƽ���򱳾���,Ĭ��ֵΪtrue
		mPlot.setDomainGridlinePaint(Color.WHITE); // ���ô�ֱ���򱳾�����ɫ
		mPlot.setDomainGridlinesVisible(true); // �����Ƿ���ʾ��ֱ���򱳾���,Ĭ��ֵΪfalse
		mPlot.setOutlinePaint(Color.WHITE);// �߽���

		// ����������
		LineAndShapeRenderer lasp = (LineAndShapeRenderer) mPlot.getRenderer();// ��ȡ��ʾ�����Ķ���
		lasp.setBaseShapesVisible(true);// ���ùյ��Ƿ�ɼ�/�Ƿ���ʾ�յ�
		lasp.setDrawOutlines(true);// ���ùյ㲻ͬ�ò�ͬ����״
		lasp.setUseFillPaint(true);// ���������Ƿ���ʾ�����ɫ
		lasp.setBaseFillPaint(Color.GREEN);// ���ùյ���ɫ
		lasp.setSeriesShape(0, new Rectangle2D.Double(-5.0D, -5.0D, 5.0D, 5.0D));// ������״
		lasp.setDrawOutlines(false);

		// ͼ����ʾ
		ChartFrame mChartFrame = new ChartFrame("�ۼƸ��ʷֲ�", mChart);
		mChartFrame.pack();
		mChartFrame.setVisible(true);
	}

	/**
	 * 	�����ۼƶȷֲ����ʣ���Ϊ����ͼ���������
	 * @param data
	 * @return
	 */
	public static CategoryDataset GetDataset(HashMap<Integer, Vector<Integer>> data) {
		DefaultCategoryDataset mDataset = new DefaultCategoryDataset();
		/*** �ۼ�ͳ�� ***/
		int total_degree;
		for(Integer index : data.keySet()) {
			total_degree = 0;
			for (Integer degree : data.keySet()) {
				if(data.get(degree).size() >= data.get(index).size())  total_degree = total_degree + data.get(degree).size();
			}
			mDataset.addValue(total_degree/data.keySet().size(), "First", index.toString());
		}
		return mDataset;
	}

	/**
	 * ��ȡ��Ϊdegree��������Ա��ID
	 * 
	 * @param degree ����ѯ�Ķ�
	 */
	private static void getDegree(Integer degree) {
		System.out
				.println("--------- ������Ϊ " + degree + " ���˹��У�" + DegreeMap.get(degree).size() + "  ��- - - - - - - - -");
		System.out.print("������");
		for (Integer index : DegreeMap.get(degree)) {
			System.out.print(index + " ");
		}
		System.out.println();
		System.out.println("------------------------------------");
	}

	/**
	 * �����Խڵ���ΪKey����ԱidΪValue��HashMap
	 */
	private static void creatDegreeMap() {
		if (DegreeMap == null)
			DegreeMap = new HashMap<Integer, Vector<Integer>>();
		for (Integer key : networks.keySet()) {
			if (!DegreeMap.containsKey(networks.get(key).size()))
				DegreeMap.put(networks.get(key).size(), new Vector<Integer>());
			DegreeMap.get(networks.get(key).size()).add(key);
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
			networks.put(peop1, new HashSet<Integer>());
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
