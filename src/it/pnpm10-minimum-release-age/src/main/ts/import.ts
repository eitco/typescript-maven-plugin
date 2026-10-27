
export interface BoxInterface<Type> {

    element: Type;
}

/**
 * .
 *
 */
export class Box<Type> {


    private element: Type;

    public constructor(object?: BoxInterface<Type>) {

        if (object) {
            this.element = object.element;
        }
    }

	/**
	 *
	 * .
	 *
	 */
    public getElement(): Type {
        return this.element;
    }


    public setElement(element: Type) {
        this.element = element;
    }

    public serialize(): any {

        let result: any = {};

        result["@type"] = "de.eitco.commons.lang.Box";


        if (this.getElement() != null) {
            result.element = this.serialize_de_eitco_commons_lang_Box_Type(this.getElement());
        }


        return result;
    }


    public serialize_de_eitco_commons_lang_Box_Type(value : any ) : any {

        return "null";
    }


    public asString() : string {

        return "";
    }
}
