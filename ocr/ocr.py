# importing the libraries
import cv2
import pytesseract
import re
import json

TESSERACT_PATH = "C:\\Program Files\\Tesseract-OCR\\tesseract.exe"
IMAGE_PATH = 'res/image3.png'

def get_ocr(path):
    pytesseract.pytesseract.tesseract_cmd = TESSERACT_PATH
    img = cv2.imread(path)
    img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)

    # For getting the text and number from image
    ocr_text = pytesseract.image_to_string(img)

    return ocr_text

# Function to extract details
def extract_details(text):
    # Extract doctor's name
    doctor_name = re.match(r"Dr\.\s?([A-Za-z\s]+)(?=\n|$)", text)
    doctor_name = doctor_name.group(1) if doctor_name else None

    # Extract doctor's designation
    possible_designations = [
        "MBBS", "MD", "M.D.", "MS", "DM", "MCh", "DNB", "BDS", "MDS", "PhD",
        "Consultant", "Surgeon", "General Physician", "Dermatologist",
        "Neurologist", "Cardiologist", "Oncologist", "Psychiatrist", "Endocrinologist",
        "Pediatrician", "Orthopedic Surgeon", "Urologist", "Gastroenterologist",
        "Nephrologist", "Rheumatologist", "Hematologist", "Pulmonologist",
        "Immunologist", "ENT Specialist", "Ophthalmologist", "Radiologist",
        "Anesthesiologist", "Pathologist", "Physiotherapist", "Chiropractor",
        "Occupational Therapist", "Speech Therapist", "Dietitian", "Nutritionist",
        "Psychologist", "Clinical Psychologist", "Gynecologist", "Obstetrician",
        "Hepatologist", "Sports Medicine Specialist", "Forensic Pathologist",
        "Infectious Disease Specialist", "Allergist", "Critical Care Specialist",
        "Emergency Medicine Specialist", "Neonatologist", "Family Medicine Physician",
        "Palliative Care Specialist", "Reproductive Endocrinologist",
        "Pain Management Specialist", "Hospitalist", "Vascular Surgeon",
        "Hand Surgeon", "Plastic Surgeon", "Cosmetic Surgeon", "Maxillofacial Surgeon",
        "Trauma Surgeon", "Bariatric Surgeon", "Podiatrist", "Epidemiologist",
        "Medical Oncologist", "Surgical Oncologist", "Radiation Oncologist",
        "Preventive Medicine Specialist", "Toxicologist", "Osteopath",
        "Geneticist", "Virologist", "Biotechnologist", "Microbiologist",
        "Pharmacologist", "Veterinarian", "Clinical Researcher",
        "Health Administrator", "Public Health Specialist", "Biostatistician",
        "Medical Informatician", "Biomedical Engineer", "Nurse Practitioner",
        "Midwife", "Clinical Nurse Specialist", "Nurse Anesthetist",
        "Cardiac Electrophysiologist", "Neuropsychiatrist", "Pediatric Surgeon",
        "Neurointerventional Radiologist", "Interventional Cardiologist",
        "Emergency Physician", "Critical Care Physician", "Ocular Oncologist"
    ]
    designation_pattern = r"\b(?:{})\b".format("|".join(re.escape(desig) for desig in possible_designations))
    designations = re.findall(designation_pattern, text)
    designations = list(set(designations))

    # Extract doctor's phone number
    doctor_phone = re.search(r"\s*(\d{10,13})", text)
    doctor_phone = doctor_phone.group(1).strip() if doctor_phone else None

    # Extract vitals with the specified parameters
    vitals_parameters = {
        # Existing Parameters
        "BP": r"BP[:\s]+([\d/]+\s?mmHg)",
        "Pulse Rate": r"Pulse Rate[:\s]+([\d]+\s?bpm)",
        "Resp Rate": r"Resp Rate[:\s]+([\d]+\s?cycles/min)",
        "SpO2": r"SpO2[:\s]+([\d]+\s?%)",
        "Weight": r"Weight[:\s]+([\d.]+\s?(?:kg|lbs))",
        "BMI": r"BMI[:\s]+([\d.]+)",
        "Blood Pressure": r"Blood Pressure[:\s]+([\d/]+\s?mmHg)",
        "PT/INR": r"PT/INR[:\s]+([\d.]+)",
        "B-type Natriuretic Peptide": r"B-type Natriuretic Peptide[:\s]+([\d.]+\s?pg/mL)",
        "Sodium": r"Sodium[:\s]+([\d.]+\s?mmol/L)",
        "Potassium": r"Potassium[:\s]+([\d.]+\s?mmol/L)",
        "Blood Glucose": r"Blood Glucose[:\s]+([\d.]+\s?mg/dL)",
        "HbA1c": r"HbA1c[:\s]+([\d.]+\s?%)",
        "Lipid Profile": r"Lipid Profile[:\s]+([^\n]+)",
        "Renal Function Tests": r"Renal Function Tests[:\s]+([^\n]+)",
        "Creatinine": r"Creatinine[:\s]+([\d.]+\s?mg/dL)",
        "eGFR": r"eGFR[:\s]+([\d.]+\s?mL/min/1.73m²)",
        "Urinalysis": r"Urinalysis[:\s]+([^\n]+)",
        "Liver Function Tests": r"Liver Function Tests[:\s]+([^\n]+)",
        "Viral Markers": r"Viral Markers[:\s]+([^\n]+)",
        "TSH": r"TSH[:\s]+([\d.]+\s?mIU/L)",
        "Free T3": r"Free T3[:\s]+([\d.]+\s?pg/mL)",
        "Free T4": r"Free T4[:\s]+([\d.]+\s?ng/dL)",
        "CRP": r"C-Reactive Protein[:\s]+([\d.]+\s?mg/L)",
        "CBC": r"CBC[:\s]+([^\n]+)",
        "Ferritin": r"Ferritin[:\s]+([\d.]+\s?ng/mL)",
        "Iron Studies": r"Iron Studies[:\s]+([^\n]+)",
        "ECG": r"ECG[:\s]+([^\n]+)",
        "Cardiac Enzymes": r"Cardiac Enzymes[:\s]+([^\n]+)",
        "Bone Density Test": r"Bone Density Test[:\s]+([^\n]+)",
        "Calcium Levels": r"Calcium Levels[:\s]+([\d.]+\s?mg/dL)",
        "Vitamin D": r"Vitamin D[:\s]+([\d.]+\s?ng/mL)",
        "Visual Acuity": r"Visual Acuity[:\s]+([^\n]+)",
        "Slit Lamp": r"Slit Lamp[:\s]+([^\n]+)",
        "Retinal Exam": r"Retinal Exam[:\s]+([^\n]+)",
        "Tonometry": r"Tonometry[:\s]+([\d.]+\s?mmHg)",
        "Culture Tests": r"Culture Tests[:\s]+([^\n]+)",
        "Contrast Sensitivity Test": r"Contrast Sensitivity Test[:\s]+([^\n]+)",
        "Triglycerides": r"Triglycerides[:\s]+([\d.]+\s?mg/dL)",
        "HDL": r"HDL[:\s]+([\d.]+\s?mg/dL)",
        "LDL": r"LDL[:\s]+([\d.]+\s?mg/dL)",
        "VLDL": r"VLDL[:\s]+([\d.]+\s?mg/dL)",
        "Total Cholesterol": r"Total Cholesterol[:\s]+([\d.]+\s?mg/dL)",
        "Albumin": r"Albumin[:\s]+([\d.]+\s?g/dL)",
        "Globulin": r"Globulin[:\s]+([\d.]+\s?g/dL)",
        "A/G Ratio": r"A/G Ratio[:\s]+([\d.]+)",
        "Bilirubin": r"Bilirubin[:\s]+([\d.]+\s?mg/dL)",
        "Alkaline Phosphatase": r"Alkaline Phosphatase[:\s]+([\d.]+\s?U/L)",
        "AST": r"AST[:\s]+([\d.]+\s?U/L)",
        "ALT": r"ALT[:\s]+([\d.]+\s?U/L)",
        "ESR": r"ESR[:\s]+([\d.]+\s?mm/hr)",
        "WBC Count": r"WBC Count[:\s]+([\d.]+\s?cells/μL)",
        "RBC Count": r"RBC Count[:\s]+([\d.]+\s?million/μL)",
        "Platelet Count": r"Platelet Count[:\s]+([\d.]+\s?cells/μL)",
        "Hemoglobin": r"Hemoglobin[:\s]+([\d.]+\s?g/dL)",
        "MCV": r"MCV[:\s]+([\d.]+\s?fL)",
        "MCH": r"MCH[:\s]+([\d.]+\s?pg)",
        "MCHC": r"MCHC[:\s]+([\d.]+\s?g/dL)",
        "RDW": r"RDW[:\s]+([\d.]+\s?%)",
        "Serum Calcium": r"Serum Calcium[:\s]+([\d.]+\s?mg/dL)",
        "Serum Magnesium": r"Serum Magnesium[:\s]+([\d.]+\s?mg/dL)",
        "Serum Phosphate": r"Serum Phosphate[:\s]+([\d.]+\s?mg/dL)",
        "Uric Acid": r"Uric Acid[:\s]+([\d.]+\s?mg/dL)",
        "Folic Acid": r"Folic Acid[:\s]+([\d.]+\s?ng/mL)",
        "Vitamin B12": r"Vitamin B12[:\s]+([\d.]+\s?pg/mL)",
        "TSH 3rd Generation": r"TSH 3rd Generation[:\s]+([\d.]+\s?mIU/L)",
        "Free Testosterone": r"Free Testosterone[:\s]+([\d.]+\s?pg/mL)",
        "Prostate-Specific Antigen": r"Prostate-Specific Antigen[:\s]+([\d.]+\s?ng/mL)",
        "D-Dimer": r"D-Dimer[:\s]+([\d.]+\s?ng/mL)",
        "Cortisol": r"Cortisol[:\s]+([\d.]+\s?μg/dL)",
        "Amylase": r"Amylase[:\s]+([\d.]+\s?U/L)",
        "Lipase": r"Lipase[:\s]+([\d.]+\s?U/L)",
        "Serum Iron": r"Serum Iron[:\s]+([\d.]+\s?μg/dL)",
        "Total Iron-Binding Capacity": r"Total Iron-Binding Capacity[:\s]+([\d.]+\s?μg/dL)",
        "Transferrin Saturation": r"Transferrin Saturation[:\s]+([\d.]+\s?%)",
        "Anti-TPO Antibodies": r"Anti-TPO Antibodies[:\s]+([\d.]+\s?IU/mL)",
        "Rheumatoid Factor": r"Rheumatoid Factor[:\s]+([\d.]+\s?IU/mL)",
        "ANA": r"ANA[:\s]+([^\n]+)",
        "dsDNA": r"dsDNA[:\s]+([^\n]+)",
        "ANCA": r"ANCA[:\s]+([^\n]+)"
    }

    vitals = {}
    for param, regex in vitals_parameters.items():
        match = re.search(regex, text, re.IGNORECASE)
        if match:
            vitals[param] = match.group(1).strip()

    # Extract complaints as a list
    complaints = re.findall(r"Complaints (.+?)(?:\n|$)", text)
    complaints_list = [complaint.strip() for complaint in complaints]

    # Extract only medication names
    medication_names = re.findall(r"\d\.\s(.*?Tablet)", text)

    # Return the extracted data as a dictionary
    return {
        "Doctor's Name": doctor_name,
        "Designation": designations,
        "Doctor's Phone": doctor_phone,
        "Vitals": vitals,
        "Complaints": complaints_list,
        "Medications": medication_names
    }

if __name__ == '__main__':
    ocr_text = get_ocr(IMAGE_PATH)
    extracted_data = extract_details(ocr_text)

    output_file = "prescription_data.json"
    with open(output_file, "w") as file:
        json.dump(extracted_data, file, indent=4)

    print(f"Data successfully extracted and saved to {output_file}")