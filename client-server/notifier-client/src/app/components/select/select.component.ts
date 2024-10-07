import { CommonModule } from '@angular/common';
import { Component, Input, Output, EventEmitter, forwardRef, OnInit } from '@angular/core';
import { ControlValueAccessor, FormControl, FormsModule, NG_VALUE_ACCESSOR } from '@angular/forms';

interface Option {
  value: number | string;
  display: string;
}

@Component({
  selector: 'crous-select',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
  ],
  templateUrl: './select.component.html',
  styleUrl: './select.component.scss',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => SelectComponent),
      multi: true,
    },
  ],
})
export class SelectComponent {
  @Input() label: string = '';
  @Input() required: boolean = false;
 
  @Input() placeholder: string = '';
  @Input() name: string = '';
  @Input() control!: FormControl | null;
  @Input() defaultValue: number | string | null = null; // Default value input
  @Input() options: Option[] = [];

  selectedValue: number | string | null = null;
  isDisabled: boolean = false;


  @Output() selectedValueChange = new EventEmitter<number | string | null>();

  private onChange: (value: number | string | null) => void = () => {};
  private onTouched: () => void = () => {};

  ngOnInit() {
    // Initialize the selectedValue with the defaultValue if no value is provided
    if (this.defaultValue !== null) {
      console.log("default value is " + this.defaultValue);
      this.selectedValue = this.defaultValue;
      this.onChange(this.defaultValue);
    }
  }

  writeValue(value: number | string | null): void {
    this.selectedValue = value ?? this.defaultValue;
    this.onChange(this.selectedValue);
  }

  registerOnChange(fn: (value: number | string | null) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.isDisabled = isDisabled;
  }

  onValueChange(event: Event): void {
    const selectElement = event.target as HTMLSelectElement;
    const value = selectElement.value ? selectElement.value : null;

    this.selectedValue = value;
    this.onChange(this.selectedValue);
    this.onTouched();
    this.selectedValueChange.emit(this.selectedValue);
  }
}
