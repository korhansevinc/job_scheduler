import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
/*
Son Degistirilme : 6 / 12 / 2022
*/
public class JobScheduler {

    public class MinHeap{

        private int capacity ;
        private int size = 0 ;
        private Job[] heap ;

        public MinHeap(int newCapacity){
            capacity = newCapacity ;
            heap = new Job[capacity];
        }

        public int getLeftChildIndex(int parentIndex){
            return (2* parentIndex) + 1 ;
        }

        public int getRightChildIndex(int parentIndex){
            return (2 * parentIndex) + 2 ;
        }

        public int getParentIndex(int childIndex){
            return (childIndex - 1 ) / 2 ;
        }

        public boolean hasLeftChild(int index){
            return getLeftChildIndex(index) < size ;
        }

        public boolean hasRightChild(int index){
            return getRightChildIndex(index) < size;
        }

        public boolean hasParent(int index){
            return getParentIndex(index) >= 0 ;
        }

        public Job leftChild(int parentIndex){
            return heap[getLeftChildIndex(parentIndex)];
        }

        public Job rightChild(int parentIndex){
            return heap[getRightChildIndex(parentIndex)];
        }

        public Job parent(int childIndex){
            return heap[getParentIndex(childIndex)];
        }

        public Job min(){
            if(size == 0)
                return null ;
            return heap[0];
        }

        public boolean isEmpty(){
            return size == 0 ;
        }

        public Job remove(Job aJob){
            if(size == 0)
                return null ;
            Job element = null ;
            for(int i = 0 ; i<size ; i++){
                if(aJob.id == heap[i].id){
                    element = heap[i];
                    heap[i] = heap[size - 1] ;
                    size -- ;
                    break ;
                }
            }
            heapifyDown();
            return element;

        }

        public Job remove(){
            if(size == 0)
                return null;
            Job element = heap[0];
            heap[0] = heap[size-1];
            size--;
            heapifyDown();
            return element ;
            
        }

        public void add(Job item){
            ensureCapacity();
            heap[size] = item ;
            size++;
            heapifyUp();
        }

        private void ensureCapacity(){
            if(size == capacity){
                heap = Arrays.copyOf(heap, capacity * 2);
                capacity = capacity * 2 ;
            }
        }

        private void swap(int index1,int index2){
            Job element = heap[index1];
            heap[index1] = heap[index2];
            heap[index2] = element ;
        }

        private void heapifyDown(){
            int index = 0 ;
            while(hasLeftChild(index)){
                int smallestChildIndex = getLeftChildIndex(index);

                if(hasRightChild(index) && rightChild(index).compareTo(leftChild(index)) < 0){
                    smallestChildIndex = getRightChildIndex(index);
                }

                if(heap[index].compareTo(heap[smallestChildIndex]) < 0){
                    break ;
                }else{
                    swap(index,smallestChildIndex);
                }
                index = smallestChildIndex ;

            }

        }

        private void heapifyUp(){
            int index = size - 1 ;

            while(hasParent(index) && parent(index).compareTo(heap[index]) > 0){
                swap(getParentIndex(index),index);
                index = getParentIndex(index);
            }
        }
        public void printHeap(){
            for (int i = 0; i <= size / 2; i++) {
                for (int j = 0; j < Math.pow(2, i) && j + Math.pow(2, i) <= size; j++) { // Each row has 2^n nodes
                    System.out.print(heap[j + (int) Math.pow(2, i) - 1] + " ");
                }
                System.out.println();
            } 
           
        }
       
        @Override
        public String toString(){
             
            StringBuilder sb = new StringBuilder("");
            if(size >2){
                int max = 0 ;
            for(int i = 0 ; i<size;i++){
                for(int j = 0 ; j<Math.pow(2,i) && j + Math.pow(2,i) <= size ; j++){
                    if(j>max)
                        max = j ;
                }
            }

            for(int i = 0 ; i<size;i++){
                for(int j = 0 ; j<Math.pow(2,i) && j + Math.pow(2,i) <= size ; j++){
                    for(int k = 0 ; (k< max / (int)Math.pow(2, i));k++){
                            sb.append(" ");
                    }
                    sb.append(heap[(j + (int)Math.pow(2, i)- 1)].id + " ");

                }
                sb.append(("\n"));
            }

            }else{
                if(size == 1){
                    sb.append(heap[0].id);
                }
                else if(size == 2){
                    sb.append(" ");
                    sb.append(heap[0].id);
                    sb.append("\n");
                    sb.append(heap[1].id);
                }   
                
            }
            
            return sb.toString();
        
        }
    }

    public class Job implements Comparable{
        int id ;
        int arrivalTime ;
        int duration;
        int workingTime ;
        ArrayList<Integer> jobDependencies ; 
        ArrayList<Integer> resourceDependencies; 

        public Job(int newId, int aT, int d){
            id = newId ;
            arrivalTime = aT ;
            duration = d ;
            workingTime = duration;
            jobDependencies = new ArrayList<>();
            resourceDependencies = new ArrayList<>();
        }

        @Override
        public String toString(){
            String str = "ID : " + id + " ArrivalTime : " + arrivalTime + " Duration : " + duration ;
            return str ;
        }

        @Override
        public int compareTo(Object o) {
            
            if(this.arrivalTime <= ((Job)o).arrivalTime){
                return -1 ;
            }
            else {
                return 1 ;
            }
        }
    }

    public class Resource {

        private int processorid ;
        private int workingTime  ;
        private Job calistirilanJob  ; // o saniyede processor'da calistirilan job
        private ArrayList<Job> jobstoDoList = new ArrayList<>(10);         
        private ArrayList<Job> finishedJobList = new ArrayList<>(10); // islemcide bitirilen joblar
        private ArrayList<Job> timeBytimeWorkingJobList = new ArrayList<>(10); // saniye saniye hangi job calisiyor kaydedilen arrayList

        public Resource(int newId){
            processorid = newId ;
            workingTime = 0 ;
            calistirilanJob = null ;
        }
        // eger calistirilan job yoksa true doner .
        public boolean isProcessorEmpty(){
            return calistirilanJob == null ;
        }

        public int getProcessorId() {
            return processorid;
        }

        public void setProcessorId(int newprocessorid) {
            this.processorid = newprocessorid;
        }

        public int getWorkingTime() {
            return workingTime;
        }
        public void setWorkingTime(int newworkingTime) {
            this.workingTime = newworkingTime;
        }

        public Job getCalistirilanJob() {
            return calistirilanJob;
        }

        public void setCalistirilanJob(Job yeniCalistirilanJob) {
            this.calistirilanJob = yeniCalistirilanJob;
        }

        public ArrayList<Job> getJobstoDoList() {
            return jobstoDoList;
        }

        public void setJobstoDoList(ArrayList<Job> jobstoDoList) {
            this.jobstoDoList = jobstoDoList;
        }

        public ArrayList<Job> getFinishedJobList() {
            return finishedJobList;
        }

        public void setFinishedJobList(ArrayList<Job> finishedJobList) {
            this.finishedJobList = finishedJobList;
        }

        public ArrayList<Job> getTimeByTimeWorkingJobList(){
            return timeBytimeWorkingJobList ;
        }

        public void setTimeByTimeWorkingJobList(ArrayList<Job> aL){
            this.timeBytimeWorkingJobList = aL;
        }

        public void addToTimeByTimeWorkingJobList(Job aJob){
            timeBytimeWorkingJobList.add(aJob);
        }

        public void addToFinishedJobs(Job aJob){
            if(!finishedJobList.contains(aJob))
                finishedJobList.add(aJob);
        }

        public Job removeFromCalistirilanJob(){
            Job element = calistirilanJob ;
            finishedJobList.add(element);
            calistirilanJob = null ;
            return element ;
        }
    }

    public MinHeap schedulerTree;
    public ArrayList<Job> dependantsQueue ; // dependency'e sahip olanlarin gecici olarak bekledigi veri yapisi.
    public ArrayList<Job> depsQueue ;
    public Integer timer = 0;
    public Integer liner = 0;
    public String filePath;
    public HashMap<Integer, LinkedList<Integer>> dependencyMap; // you can change Hashmap as Hashmap<Integer,Integer> or any other type
    public ArrayList<Job> jobs ; 
    public Resource[] processors ; // islemciler
    Scanner scanner ;

    public JobScheduler(String filePath) {
        this.filePath = filePath;
        schedulerTree = new MinHeap(10);
        dependencyMap = new HashMap<>();
        dependantsQueue = new ArrayList<Job>();
        depsQueue = new ArrayList<Job>();
        jobs = new ArrayList<>();
        File file = new File(filePath);
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("Jobs.txt cannot be reached");
            e.printStackTrace();
        }
    }

    public void insertDependencies(String dependencyPath){
        File file = new File(dependencyPath);
        Scanner dependenciesScanner = null ;
        try {
            dependenciesScanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("Cannot find the dependencies path");
            e.printStackTrace();
        }
        while(dependenciesScanner.hasNext()){
            int str1 = Integer.parseInt(dependenciesScanner.next());
            int str2 = Integer.parseInt(dependenciesScanner.next());
            LinkedList<Integer> deps = dependencyMap.get(str1);

            if(deps == null){
                deps = new LinkedList<Integer>();
                deps.add(str2);
                dependencyMap.put(str1, deps);
            }else{
                deps.add(str2);
                dependencyMap.put(str1, deps);
            }
        }
    }
    
    public boolean stillContinues(){
        if(scanner.hasNextLine()){
            liner++;
            timer++;
            return true; 
        }
        return false;
    }
    // returns dependencies of a job within a linked list , returns null if there is no such dependencies.
    public LinkedList<Integer> checkDependencies(Job item){
        int itemId = item.id ;
        LinkedList<Integer> valList = dependencyMap.get(itemId);
        if(valList == null)
            return null ;
        return valList ;

    }
    public void run(){

        for(int i = 0 ; i< processors.length;i++){
            Job element = processors[i].calistirilanJob;
            if(element != null){
                element.workingTime -=1 ;
                processors[i].addToTimeByTimeWorkingJobList(element);
                if(element.workingTime <=0){
                    processors[i].removeFromCalistirilanJob();
                }
            }else{
                Job aNullJob = new Job(0, 0, 0);
                processors[i].addToTimeByTimeWorkingJobList(aNullJob);
            }
        }

        for(int i = 0 ; i< processors.length;i++){
            for(JobScheduler.Job element : processors[i].getFinishedJobList()){
                if(element != null){
                    for(LinkedList<Integer> list : dependencyMap.values()){
                        if(list.contains(element.id)){
                            list.remove((Integer)element.id);
                        }
                    }
                }
            } 
        }   
        for(int i = 0 ; i< schedulerTree.size; i++){
            Job element = schedulerTree.heap[i];
            LinkedList<Integer> dependencyList = checkDependencies(element);
            if(dependencyList == null || dependencyList.isEmpty()){
                for(int j =0 ; j<processors.length;j++){
                    boolean hepsiCalisiyorMu = checkAllProcessorsWorking();
                    if(hepsiCalisiyorMu){
                        element.resourceDependencies.add(timer);
                        break ;
                    }
                    if(processors[j].isProcessorEmpty()){
                        processors[j].setCalistirilanJob(element);
                        schedulerTree.remove(schedulerTree.heap[i]); 
                        i--;
                        break ;
                    }
                }
            }
            else{
                //dependencyList is not null : Baska veri yapisina kaydet...
                dependantsQueue.add(element);
                for(int z = 0 ; z< dependencyList.size();z++){
                    if(!element.jobDependencies.contains(dependencyList.get(z)))
                    element.jobDependencies.add(dependencyList.get(z));
                }
                schedulerTree.remove(schedulerTree.heap[i]);
            }
        }

        for(int i = 0; i< dependantsQueue.size();i++){
            depsQueue.add(dependantsQueue.get(i));
            schedulerTree.add(dependantsQueue.remove(i));
        }
    }
 // checks if all processors are working 
 // returns true if all working , returns false if at least one processor is not working.
    private boolean checkAllProcessorsWorking() {
        for(int i = 0 ; i< processors.length;i++){
            if(processors[i].calistirilanJob == null){
                return false ;
            }
        }
        return true ;
    }

    public void setResourcesCount(Integer count){
        this.processors = new Resource[count];
        for(int i = 0 ; i<count;i++){
            processors[i] = new Resource(i+1);
        }
    }

    public void insertJob(){
        String input = scanner.nextLine();
        int jobID1 = 0, jobDuration  = 0;
        Job newJob = null ;
        if(!input.equals("no job")){  
            input = input.trim();
            for(int i = 0 ; i< input.length();i++){
                if(input.charAt(i) == ' '){
                    jobID1 =Integer.parseInt(input.substring(0, i));
                    input = input.substring(i);
                    input = input.trim();
                    jobDuration= Integer.parseInt(input);
                    newJob = new Job(jobID1,timer,jobDuration);
                    schedulerTree.add(newJob);
                }
           }
        }
    }

    public void completedJobs(){
        String str = "completed jobs ";
        for(int i = 0 ; i<processors.length;i++){
            for(int j = 0 ; j< processors[i].finishedJobList.size();j++){
                str  = str + processors[i].finishedJobList.get(j).id + ",";
            }
        }
        if(str.charAt(str.length()-1) == ','){
            str = str.substring(0, str.length()-1);
        }
        System.out.println(str);
    }

    public void dependencyBlockedJobs(){
        String str = "dependency blocked jobs " ;
        
        for(int i = 0 ; i<schedulerTree.size;i++){
            LinkedList<Integer> tempList = dependencyMap.get(schedulerTree.heap[i].id);
            if(tempList ==null || tempList.isEmpty()){
                // burada bir seye gerek yok...
            }   
            else{
                str = str + "(" + schedulerTree.heap[i].id + "," + tempList.toString() + ")" ;
            }
        }
        System.out.println(str);
    }

    public void resourceBlockedJobs(){
        String str = "resource blocked jobs";
        for(int i = 0 ; i < schedulerTree.size;i++){
            Job element = schedulerTree.heap[i];
            for(int j = 0 ; j< element.resourceDependencies.size();j++){
                if(timer == element.resourceDependencies.get(j)){
                    str = str + " " + element.id ;
                }
            }   
        }
        System.out.println(str);
    }

    public void workingJobs(){
        String str = "working jobs ";

        for(int i = 0 ; i<processors.length;i++){
            if(processors[i].calistirilanJob != null){
                str = str + "("+processors[i].calistirilanJob.id + "," + processors[i].processorid + ") "; 
            }
        }
        System.out.println(str);
    }

    public void runAllRemaining(){
        
        while(checkAllProcessorsWorking()){
            timer++;
            run();
        }
    }
    public void allTimeLine(){
        String str = " \t";
        for(int i = 0 ; i<processors.length;i++){
            str = str + "R" + (i+1);
            str = str + "\t";
        }
        str = str + "\n";
        for(int i = 0 ; i< timer ; i++){
            if(i != 0){
                str = str + (i)  + "\t";
                for(int j = 0 ; j< processors.length;j++){
                    if(processors[j].timeBytimeWorkingJobList.get(i).id != 0 ){
                        str = str + processors[j].timeBytimeWorkingJobList.get(i).id + "\t";
                    }else{
                        str = str + " " + "\t";
                    }
                }
                str = str + "\n";
            }
        }
        System.out.println(str);
    }

    public String toString(){
        return schedulerTree.toString();
    }
}