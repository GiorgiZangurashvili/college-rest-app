[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/2WXZNEML)
# Java Spring Bootcamp 02 homework
### Week 17, Assignment 1
#### College Crud app

---
პატარა კოლეჯისთვის წერთ აპლიკაციის BEს.  

კოლეჯს აინტერესებს შეინახოს მონაცემები სტუდენტის, საგნების და სტუდენტის ქულების შესახებ.  
სტუდენტის მინიმალური ველები ( სახელი, დაბ.თარიღი, Email)  
კურსის(საგნის)* მინიმალური ველები ( კურსის სახელი, კოდი, ამოქმედების(ძალაში შესვლის) თარიღი (_EFF_FROM_), გაუქმების თარიღი (_EFF_TO_))  // _Effective date_  
* სტუდენტს ერთიდაიგივე კურსის არჩევა რამდენჯერაც შეუძლია კონფიგურურებადი უნდა იყოს.
* სტუდენტს კურსისი არჩევა შეუძლია კურსის სასწავლო პერიოდის დაწყებამდე კონკრეტული პერიოდის განმავლობაში (კონფიგურირებადია)
* სტუდენტს კურსისი არჩევა შეუძლია კურსის სასწავლო პერიოდის დაწყებიდან კონკრეტული პერიოდის განმავლობაში (კონფიგურირებადია)
* უნდა ინახებოდეს ინფორმაცია სტუდენმა კურსი როდის აირჩია  ( თუ რამდენიმეჯერ - მაშინ ყველა)
* უნდა ინახებოდეს ინფორმაცია სტუდენტის შეფასების შესახებ. (როდის რა ქულა აიღო)
* სტუენტს არ შეუძლია აირჩიოს უკვე წარმატებით დასრულებული კურსი (იგულისხმება დააგროვა>= 51 ქულა)

თქვენი ამოცანაა
* შექმნათ REST web app რომელიც აკმაყოფილებს ზემოთ აღწერილ პირობებს.
* დააკონფიგურუროთ სვაგერი. ( აღწეროთ როგორც კონტოლერების მეთოდები, დასაბრუნებელი ჰტტპ კოდები, ასევე მოდელების ველების მნიშვნელობები.)
* შექმნათ ბაზა და შესაბამისი entityები.
* სწორად დაადოთ ვალიდაციები და სწორად დააბრუნოთ შეტყობინებები.

დამატებითი ფუნქციონალი ADVANCED
* უნდა შეიძლებოდეს სტუდნტების ძებნა (ნებისმიერი ქვემოთ ჩამოთვლილი კობნინაციით)(criteria query)
  * ქულების მიხედვით ( მაგ: ვის აქვს A, ან C. ( A = [91-100]; B = [81-90]...).  როგორც 1 ისე რამდენიმე ქულის მიხედვით) (აქ იგულისხმება სტუდენტები რომელბაც უკვე დაასრულეს კურსი)
  * საგნების მიხედვით (საგნის კოდის მიხედვით. როგორც 1 ისე რამდენიმე საგნის მიხედვით)
  * ასაკის მიხედვით.


___
კურსის(საგნის) გაუქმების თარიღი: აქ იგულისხმება რომ კურსს კოლეჯს შეუძლია შემოიტანოს კურსი, და თქვას რომ დღეიდან მომდევნო 10 წელი ასწავლის ამ კურსს. ამ შემთხვევაში ამოქმედების თარიღი იქნება 2023 წელს გაუქმების თარიღი - 2033 წელს.  
ასევე კურსს აქვს კურსის სასწავლო პერიოდის დაწყების და დასრულების თარიღებიც. თუ ამ შუალედში სტუდენმა მოაგროვა 51 ან მეტი ქულა, მაშინ მან წარმატებით დაასრულა კურსი.
