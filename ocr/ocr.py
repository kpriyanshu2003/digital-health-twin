# importing the libraries
import cv2
import pytesseract
import re
import json

TESSERACT_PATH = "C:\\Program Files\\Tesseract-OCR\\tesseract.exe"
IMAGE_PATH = "res/image3.png"


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
        "MBBS",
        "MD",
        "M.D.",
        "MS",
        "DM",
        "MCh",
        "DNB",
        "BDS",
        "MDS",
        "PhD",
        "Consultant",
        "Surgeon",
        "General Physician",
        "Dermatologist",
        "Neurologist",
        "Cardiologist",
        "Oncologist",
        "Psychiatrist",
        "Endocrinologist",
        "Pediatrician",
        "Orthopedic Surgeon",
        "Urologist",
        "Gastroenterologist",
        "Nephrologist",
        "Rheumatologist",
        "Hematologist",
        "Pulmonologist",
        "Immunologist",
        "ENT Specialist",
        "Ophthalmologist",
        "Radiologist",
        "Anesthesiologist",
        "Pathologist",
        "Physiotherapist",
        "Chiropractor",
        "Occupational Therapist",
        "Speech Therapist",
        "Dietitian",
        "Nutritionist",
        "Psychologist",
        "Clinical Psychologist",
        "Gynecologist",
        "Obstetrician",
        "Hepatologist",
        "Sports Medicine Specialist",
        "Forensic Pathologist",
        "Infectious Disease Specialist",
        "Allergist",
        "Critical Care Specialist",
        "Emergency Medicine Specialist",
        "Neonatologist",
        "Family Medicine Physician",
        "Palliative Care Specialist",
        "Reproductive Endocrinologist",
        "Pain Management Specialist",
        "Hospitalist",
        "Vascular Surgeon",
        "Hand Surgeon",
        "Plastic Surgeon",
        "Cosmetic Surgeon",
        "Maxillofacial Surgeon",
        "Trauma Surgeon",
        "Bariatric Surgeon",
        "Podiatrist",
        "Epidemiologist",
        "Medical Oncologist",
        "Surgical Oncologist",
        "Radiation Oncologist",
        "Preventive Medicine Specialist",
        "Toxicologist",
        "Osteopath",
        "Geneticist",
        "Virologist",
        "Biotechnologist",
        "Microbiologist",
        "Pharmacologist",
        "Veterinarian",
        "Clinical Researcher",
        "Health Administrator",
        "Public Health Specialist",
        "Biostatistician",
        "Medical Informatician",
        "Biomedical Engineer",
        "Nurse Practitioner",
        "Midwife",
        "Clinical Nurse Specialist",
        "Nurse Anesthetist",
        "Cardiac Electrophysiologist",
        "Neuropsychiatrist",
        "Pediatric Surgeon",
        "Neurointerventional Radiologist",
        "Interventional Cardiologist",
        "Emergency Physician",
        "Critical Care Physician",
        "Ocular Oncologist",
    ]
    designation_pattern = r"\b(?:{})\b".format(
        "|".join(re.escape(desig) for desig in possible_designations)
    )
    designations = re.findall(designation_pattern, text)
    designations = list(set(designations))

    # Extract doctor's phone number
    doctor_phone = re.search(r"\s*(\d{10,13})", text)
    doctor_phone = doctor_phone.group(1).strip() if doctor_phone else None

    # Extract vitals with the specified parameters
    vitals_parameters = {
        "BP": r"BP[:\s]+([\d/]+)",  # Excludes "mmHg"
        "Pulse Rate": r"Pulse Rate[:\s]+([\d]+)",  # Excludes "bpm"
        "Pulse Pattern": r"Pulse Pattern\s*:\s*([^\s]+)",  # No units
        "Resp Rate": r"Resp Rate[:\s]+([\d]+)",  # Excludes "cycles/min"
        "Resp Pattern": r"Resp Pattern\s*:\s*([^\s]+)",  # No units
        "Temperature": r"(?:Temperature)\s*[:\-]?\s*(\d{1,3})",  # Excludes "Celsius"
        "SpO2": r"SpO2[:\s]+([\d]+)",  # Excludes "%"
        "RBS": r"(?:RBS)\s*[:\-]?\s*(\d{1,3})",  # Excludes "%"
        "Weight": r"Weight[:\s]+([\d.]+)",  # Excludes "kg" or "lbs"
        "BMI": r"BMI[:\s]+([\d.]+)",  # No units
        "Blood Pressure": r"Blood Pressure[:\s]+([\d/]+)",  # Excludes "mmHg"
        "PT/INR": r"PT/INR[:\s]+([\d.]+)",  # No units
        "B-type Natriuretic Peptide": r"B-type Natriuretic Peptide[:\s]+([\d.]+)",  # Excludes "pg/mL"
        "Sodium": r"Sodium[:\s]+([\d.]+)",  # Excludes "mmol/L"
        "Potassium": r"Potassium[:\s]+([\d.]+)",  # Excludes "mmol/L"
        "Blood Glucose": r"Blood Glucose[:\s]+([\d.]+)",  # Excludes "mg/dL"
        "HbA1c": r"HbA1c[:\s]+([\d.]+)",  # Excludes "%"
        "Creatinine": r"Creatinine[:\s]+([\d.]+)",  # Excludes "mg/dL"
        "eGFR": r"eGFR[:\s]+([\d.]+)",  # Excludes "mL/min/1.73m²"
        "Calcium Levels": r"Calcium Levels[:\s]+([\d.]+)",  # Excludes "mg/dL"
        "Vitamin D": r"Vitamin D[:\s]+([\d.]+)",  # Excludes "ng/mL"
        "Triglycerides": r"Triglycerides[:\s]+([\d.]+)",  # Excludes "mg/dL"
        "HDL": r"HDL[:\s]+([\d.]+)",  # Excludes "mg/dL"
        "LDL": r"LDL[:\s]+([\d.]+)",  # Excludes "mg/dL"
        "VLDL": r"VLDL[:\s]+([\d.]+)",  # Excludes "mg/dL"
        "Total Cholesterol": r"Total Cholesterol[:\s]+([\d.]+)",  # Excludes "mg/dL"
        "Albumin": r"Albumin[:\s]+([\d.]+)",  # Excludes "g/dL"
        "Globulin": r"Globulin[:\s]+([\d.]+)",  # Excludes "g/dL"
        "A/G Ratio": r"A/G Ratio[:\s]+([\d.]+)",  # No units
        "Bilirubin": r"Bilirubin[:\s]+([\d.]+)",  # Excludes "mg/dL"
        "Alkaline Phosphatase": r"Alkaline Phosphatase[:\s]+([\d.]+)",  # Excludes "U/L"
        "AST": r"AST[:\s]+([\d.]+)",  # Excludes "U/L"
        "ALT": r"ALT[:\s]+([\d.]+)",  # Excludes "U/L"
        "ESR": r"ESR[:\s]+([\d.]+)",  # Excludes "mm/hr"
        "WBC Count": r"WBC Count[:\s]+([\d.]+)",  # Excludes "cells/μL"
        "RBC Count": r"RBC Count[:\s]+([\d.]+)",  # Excludes "million/μL"
        "Platelet Count": r"Platelet Count[:\s]+([\d.]+)",  # Excludes "cells/μL"
        "Hemoglobin": r"Hemoglobin[:\s]+([\d.]+)",  # Excludes "g/dL"
        "MCV": r"MCV[:\s]+([\d.]+)",  # Excludes "fL"
        "MCH": r"MCH[:\s]+([\d.]+)",  # Excludes "pg"
        "MCHC": r"MCHC[:\s]+([\d.]+)",  # Excludes "g/dL"
        "RDW": r"RDW[:\s]+([\d.]+)",  # Excludes "%"
        "Serum Calcium": r"Serum Calcium[:\s]+([\d.]+)",  # Excludes "mg/dL"
        "Serum Magnesium": r"Serum Magnesium[:\s]+([\d.]+)",  # Excludes "mg/dL"
        "Serum Phosphate": r"Serum Phosphate[:\s]+([\d.]+)",  # Excludes "mg/dL"
        "Uric Acid": r"Uric Acid[:\s]+([\d.]+)",  # Excludes "mg/dL"
        "Folic Acid": r"Folic Acid[:\s]+([\d.]+)",  # Excludes "ng/mL"
        "Vitamin B12": r"Vitamin B12[:\s]+([\d.]+)",  # Excludes "pg/mL"
        "TSH 3rd Generation": r"TSH 3rd Generation[:\s]+([\d.]+)",  # Excludes "mIU/L"
        "Free Testosterone": r"Free Testosterone[:\s]+([\d.]+)",  # Excludes "pg/mL"
        "Prostate-Specific Antigen": r"Prostate-Specific Antigen[:\s]+([\d.]+)",  # Excludes "ng/mL"
        "D-Dimer": r"D-Dimer[:\s]+([\d.]+)",  # Excludes "ng/mL"
        "Cortisol": r"Cortisol[:\s]+([\d.]+)",  # Excludes "μg/dL"
        "Amylase": r"Amylase[:\s]+([\d.]+)",  # Excludes "U/L"
        "Lipase": r"Lipase[:\s]+([\d.]+)",  # Excludes "U/L"
        "Serum Iron": r"Serum Iron[:\s]+([\d.]+)",  # Excludes "μg/dL"
        "Total Iron-Binding Capacity": r"Total Iron-Binding Capacity[:\s]+([\d.]+)",  # Excludes "μg/dL"
        "Transferrin Saturation": r"Transferrin Saturation[:\s]+([\d.]+)",  # Excludes "%"
        "Anti-TPO Antibodies": r"Anti-TPO Antibodies[:\s]+([\d.]+)",  # Excludes "IU/mL"
        "Rheumatoid Factor": r"Rheumatoid Factor[:\s]+([\d.]+)",  # Excludes "IU/mL"
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
        "doctorName": doctor_name,
        "designation": designations,
        "doctorPhone": doctor_phone,
        "vitals": vitals,
        "complaints": complaints_list,
        "medications": medication_names,
    }


if __name__ == "__main__":
    ocr_text = get_ocr(IMAGE_PATH)
    extracted_data = extract_details(ocr_text)

    output_file = "prescription_data.json"
    with open(output_file, "w") as file:
        json.dump(extracted_data, file, indent=4)

    print(f"Data successfully extracted and saved to {output_file}")
