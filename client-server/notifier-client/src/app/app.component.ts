import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { InputComponent } from './components/input/input.component';
import { SelectComponent } from './components/select/select.component';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';

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
  crousList = [
    { value: 'aix-marseille-avignon', display: 'CROUS Aix-Marseille-Avignon' },
    { value: 'amiens-picardie', display: 'CROUS Amiens-Picardie' },
    { value: 'antilles-guyane', display: 'CROUS Antilles-Guyane' },
    { value: 'besancon', display: 'CROUS Besançon' },
    { value: 'bordeaux-aquitaine', display: 'CROUS Bordeaux-Aquitaine' },
    { value: 'clermont-auvergne', display: 'CROUS Clermont-Auvergne' },
    { value: 'corse', display: 'CROUS Corse' },
    { value: 'creteil', display: 'CROUS Créteil' },
    { value: 'dijon', display: 'CROUS Dijon' },
    { value: 'grenoble-alpes', display: 'CROUS Grenoble-Alpes' },
    { value: 'lille-nord-pas-de-calais', display: 'CROUS Lille-Nord-Pas-de-Calais' },
    { value: 'limoges', display: 'CROUS Limoges' },
    { value: 'lyon-saint-etienne', display: 'CROUS Lyon-Saint-Étienne' },
    { value: 'montpellier-occitanie', display: 'CROUS Montpellier-Occitanie' },
    { value: 'nancy-metz', display: 'CROUS Nancy-Metz' },
    { value: 'nantes-pays-de-la-loire', display: 'CROUS Nantes-Pays de la Loire' },
    { value: 'nice-toulon', display: 'CROUS Nice-Toulon' },
    { value: 'orleans-tours', display: 'CROUS Orléans-Tours' },
    { value: 'paris', display: 'CROUS Paris' },
    { value: 'poitiers', display: 'CROUS Poitiers' },
    { value: 'reims', display: 'CROUS Reims' },
    { value: 'rennes-bretagne', display: 'CROUS Rennes-Bretagne' },
    { value: 'rouen-normandie', display: 'CROUS Rouen-Normandie' },
    { value: 'strasbourg', display: 'CROUS Strasbourg' },
    { value: 'toulouse-occitanie', display: 'CROUS Toulouse-Occitanie' },
    { value: 'versailles', display: 'CROUS Versailles' }
  ];


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

}
