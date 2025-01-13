import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { InputComponent } from './components/input/input.component';
import { SelectComponent } from './components/select/select.component';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';


interface OptionCrous {
  value: string;
  display: string;
}

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    InputComponent,
    SelectComponent,
    CommonModule,
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'notifier-client';

  logo = "./../../assets/logo.png";
  ensias = "./../../assets/ensias.png";
  isima = "./../../assets/inp-isima.png";

  registerForm: FormGroup;
  baseListCrous: OptionCrous[] = this.createCrousList();
  crousListToDisplay: OptionCrous[] = this.createCrousList();
  selectedCrous: string [] = [];

  private createCrousList(): OptionCrous[] {
    return [
      { value: '13000', display: 'CROUS Aix-Marseille-Avignon' },
      { value: '80000', display: 'CROUS Amiens-Picardie' },
      { value: '97100', display: 'CROUS Antilles-Guyane' },
      { value: '25000', display: 'CROUS Besançon' },
      { value: '33000', display: 'CROUS Bordeaux-Aquitaine' },
      { value: '63000', display: 'CROUS Clermont-Auvergne' },
      { value: '20000', display: 'CROUS Corse' },
      { value: '94000', display: 'CROUS Créteil' },
      { value: '21000', display: 'CROUS Dijon' },
      { value: '38000', display: 'CROUS Grenoble-Alpes' },
      { value: '59000', display: 'CROUS Lille-Nord-Pas-de-Calais' },
      { value: '87000', display: 'CROUS Limoges' },
      { value: '69000', display: 'CROUS Lyon-Saint-Étienne' },
      { value: '34000', display: 'CROUS Montpellier-Occitanie' },
      { value: '54000', display: 'CROUS Nancy-Metz' },
      { value: '44000', display: 'CROUS Nantes-Pays de la Loire' },
      { value: '06000', display: 'CROUS Nice-Toulon' },
      { value: '45000', display: 'CROUS Orléans-Tours' },
      { value: '75000', display: 'CROUS Paris' },
      { value: '86000', display: 'CROUS Poitiers' },
      { value: '51100', display: 'CROUS Reims' },
      { value: '35000', display: 'CROUS Rennes-Bretagne' },
      { value: '76000', display: 'CROUS Rouen-Normandie' },
      { value: '67000', display: 'CROUS Strasbourg' },
      { value: '31000', display: 'CROUS Toulouse-Occitanie' },
      { value: '78000', display: 'CROUS Versailles' }
    ];
  }

  constructor(fb: FormBuilder) {
    this.registerForm = fb.group({
      fullName: ['', Validators.required],
      email: ['', Validators.required],
      telephone: ['', Validators.required],
      crous:['', Validators.required]
    });
  }

  getControl(controlName: string): FormControl | null {
    return this.registerForm.get(controlName) as FormControl;
  }

  handleButtonClick(): void{
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
    } else {
      console.log(this.registerForm.value);
      this.registerForm.reset();
      alert('Form submitted successfully!');
    }
  }

  handleAddSelectedCrous(option: string): void {
    option = option.substring(3, option.length);
    if(this.selectedCrous.indexOf(option)==-1)
      this.selectedCrous.push(option);
    this.crousListToDisplay = this.crousListToDisplay.filter(c => !c.value.toString().includes(option) );
  }

  handleRemoveSelectedCrous(option: string): void {
    // Remove the selected option from the selectedCrous array
    this.selectedCrous = this.selectedCrous.filter(c => c !== option);

    // Add the removed CROUS back to the display list
    const removedCrous: OptionCrous | undefined = this.baseListCrous.find(c => c.value === option);

    if (removedCrous) {
      this.crousListToDisplay.push(removedCrous);
    }
  }

}
