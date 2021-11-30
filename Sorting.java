package UsefulFunction;

import java.util.ArrayList;
import java.util.List;

public class Sorting {
	
	private int times = 20;
	private List<Integer> initialList = new ArrayList<>();
	
	public static void main(String[] args) {
		Sorting sorting = new Sorting();
		sorting.selectionSortMinToMax(sorting.getInitialList());
		sorting.selectionSortMaxToMin(sorting.getInitialList());
		sorting.bubbleSortMinToMax(sorting.getInitialList());
		sorting.bubbleSortMaxToMin(sorting.getInitialList());
		sorting.insertionSortMinToMax(sorting.getInitialList());
		sorting.insertionSortMaxToMin(sorting.getInitialList());
		sorting.cocktailSortMinToMax(sorting.getInitialList());
		sorting.cocktailSortMaxToMin(sorting.getInitialList());
	}
	
	public Sorting() {
		setInitialList();
		System.out.println("Initial List = ");
		for(int number : initialList) {
			System.out.print(number + " ");
		}
		System.out.println("\n");
	}
	
	private void setInitialList() {
		for(int i = 0; i < times; i++) {
			double randomNo = 200 * Math.random();
			initialList.add((int) randomNo);
		}
	}
	
	private void selectionSortMinToMax(List<Integer> list) {
		long start = System.currentTimeMillis();
		int length = list.size(), mini = 0;
		for(int i = 0; i < length; i++) {
			int min = i;
			for(int j = i + 1; j < length; j++) {
				if(list.get(j) < list.get(min)) {
					min = j;
				}
			}
			if(min != i) {
				mini = list.get(min);
				list.set(min, list.get(i));
				list.set(i, mini);
			}
		}
		System.out.println("Selection Sort Min To Max Cost Time = " + (System.currentTimeMillis() - start) + " ms");
		System.out.println("Selection Sort Min To Max Final List = ");
		for(int number : list) {
			System.out.print(number + " ");
		}
		System.out.println("\n");
	}
	
	private void selectionSortMaxToMin(List<Integer> list) {
		long start = System.currentTimeMillis();
		int length = list.size(), mini = 0;
		for(int i = 0; i < length; i++) {
			int max = i;
			for(int j = i + 1; j < length; j++) {
				if(list.get(j) > list.get(max)) {
					max = j;
				}
			}
			if(max != i) {
				mini = list.get(max);
				list.set(max, list.get(i));
				list.set(i, mini);
			}
		}
		System.out.println("Selection Sort Max To Min Cost Time = " + (System.currentTimeMillis() - start) + " ms");
		System.out.println("Selection Sort Max To Min Final List = ");
		for(int number : list) {
			System.out.print(number + " ");
		}
		System.out.println("\n");
	}
	
	private void bubbleSortMinToMax(List<Integer> list) {
		int length = list.size();
		long start = System.currentTimeMillis();
		boolean flag = true;
		while (flag) {
			int changeTimes = 0;
			for(int i = 0; i < length - 1; i++) {
				if(list.get(i) > list.get(i + 1)) {
					int min = list.get(i + 1);
					list.set(i + 1, list.get(i));
					list.set(i, min);
					changeTimes += 1;
				}
			}
			if(changeTimes == 0) {
				flag = false;
			}
		}
		System.out.println("Bubble Sort Min To Max Cost Time = " + (System.currentTimeMillis() - start) + " ms");
		System.out.println("Bubble Sort Min To Max Final List = ");
		for(int number : list) {
			System.out.print(number + " ");
		}
		System.out.println("\n");
	}
	
	private void bubbleSortMaxToMin(List<Integer> list) {
		int length = list.size();
		long start = System.currentTimeMillis();
		boolean flag = true;
		while (flag) {
			int changeTimes = 0;
			for(int i = 0; i < length - 1; i++) {
				if(list.get(i) < list.get(i + 1)) {
					int max = list.get(i + 1);
					list.set(i + 1, list.get(i));
					list.set(i, max);
					changeTimes += 1;
				}
			}
			if(changeTimes == 0) {
				flag = false;
			}
		}
		System.out.println("Bubble Sort Max To Min Cost Time = " + (System.currentTimeMillis() - start) + " ms");
		System.out.println("Bubble Sort Max To Min Final List = ");
		for(int number : list) {
			System.out.print(number + " ");
		}
		System.out.println("\n");
	}
	
	private void insertionSortMinToMax(List<Integer> list) {
		int length = list.size();
		long start = System.currentTimeMillis();
		List<Integer> finalList = new ArrayList<>();
		finalList.add(list.get(0));
		for(int i = 1; i < length; i++) {
			int nonSortNo = list.get(i);
			int j = i - 1;
			while(j >= 0 && list.get(j) > nonSortNo) {
				list.set(j + 1, list.get(j));
				j--;
			}
			list.set(j + 1, nonSortNo);
		}
		System.out.println("Insertion Sort Min To Max Cost Time = " + (System.currentTimeMillis() - start) + " ms");
		System.out.println("Insertion Sort Min To Max Final List = ");
		for(int number : list) {
			System.out.print(number + " ");
		}
		System.out.println("\n");
	}
	
	private void insertionSortMaxToMin(List<Integer> list) {
		int length = list.size();
		long start = System.currentTimeMillis();
		List<Integer> finalList = new ArrayList<>();
		finalList.add(list.get(0));
		for(int i = 1; i < length; i++) {
			int nonSortNo = list.get(i);
			int j = i - 1;
			while(j >= 0 && list.get(j) < nonSortNo) {
				list.set(j + 1, list.get(j));
				j--;
			}
			list.set(j + 1, nonSortNo);
		}
		System.out.println("Insertion Sort Max To Min Cost Time = " + (System.currentTimeMillis() - start) + " ms");
		System.out.println("Insertion Sort Max To Min Final List = ");
		for(int number : list) {
			System.out.print(number + " ");
		}
		System.out.println("\n");
	}
	
	private void cocktailSortMinToMax(List<Integer> list) {
		int length = list.size();
		long start = System.currentTimeMillis();
		boolean flag = true;
		do {
			flag = true;
			for(int i = 0; i < length - 1; i++) {
				if(list.get(i + 1) < list.get(i)) {
					int left = list.get(i);
					list.set(i, list.get(i + 1));
					list.set(i + 1, left);
					flag = false;
				}
			}
			for(int i = length - 1; i > 0; i--) {
				if(list.get(i - 1) > list.get(i)) {
					int right = list.get(i);
					list.set(i, list.get(i - 1));
					list.set(i - 1, right);
					flag = false;
				}
			}
			
		} while (!flag);
		System.out.println("Cocktail Sort Min To Max Cost Time = " + (System.currentTimeMillis() - start) + " ms");
		System.out.println("Cocktail Sort Min To Max Final List = ");
		for(int number : list) {
			System.out.print(number + " ");
		}
		System.out.println("\n");
	}
	
	private void cocktailSortMaxToMin(List<Integer> list) {
		int length = list.size();
		long start = System.currentTimeMillis();
		boolean flag = true;
		do {
			flag = true;
			for(int i = 0; i < length - 1; i++) {
				if(list.get(i + 1) > list.get(i)) {
					int left = list.get(i);
					list.set(i, list.get(i + 1));
					list.set(i + 1, left);
					flag = false;
				}
			}
			for(int i = length - 1; i > 0; i--) {
				if(list.get(i - 1) < list.get(i)) {
					int right = list.get(i);
					list.set(i, list.get(i - 1));
					list.set(i - 1, right);
					flag = false;
				}
			}
			
		} while (!flag);
		System.out.println("Cocktail Sort Max To Min Cost Time = " + (System.currentTimeMillis() - start) + " ms");
		System.out.println("Cocktail Sort Max To Min Final List = ");
		for(int number : list) {
			System.out.print(number + " ");
		}
		System.out.println("\n");
	}
	
	public List<Integer> getInitialList(){
		return initialList;
	}
}
