class View {
  static render(state){
    return TreeBuilder.build(h=>
      h("div", {}, "TODO"
      , h("button", {
            onclick: ()=>{ __p.onclick_post(); }
          }, "post"
        )
      )
    );
  }
}

class Page {
  constructor(){
    this.state = {};
  }

  init(){
    console.log("init");

    __g.api_v2("get", "/api/sample", {
        fooBar: 123, b: { c: 456 }
      }, (result)=>{
      __g.unguard();
      puts(result);
      Object.assign(this.state, result);

      this.render();

    }, (errors)=>{
      __g.unguard();
      __g.printApiErrors(errors);
      alert("Check console.");
    });
  }

  getTitle(){
    return "scalatra-template index";
  }

  render(){
    $("#main").empty().append(View.render(this.state));
  }

  onclick_post(){
    __g.api_v2("post", "/api/sample", {
        fooBar: 123, b: { c: 456 }
      }, (result)=>{
      __g.unguard();
      puts(result);
    }, (errors)=>{
      __g.unguard();
      __g.printApiErrors(errors);
      alert("Check console.");
    });
  }
}
	
__g.ready(new Page());
